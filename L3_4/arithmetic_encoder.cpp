#include <cstdlib>
#include <fstream>
#include <iostream>
#include <vector>
#include <queue>

struct probability
{
	unsigned long long lower;
	unsigned long long upper;
	unsigned long long cumulative;
};

std::vector<unsigned long long> initProbability(int signsNumber) {
	std::vector<unsigned long long> cumulativeFrequency(signsNumber + 1); // codes + total count

	for (size_t i = 0; i < cumulativeFrequency.size(); ++i) {
		cumulativeFrequency[i] = i;
	}

	return cumulativeFrequency;
}

void updateRanges(std::vector<unsigned long long>& cumulativeFrequency, int c) {
	
	for (size_t i = c + 1; i < cumulativeFrequency.size(); ++i) {
		++cumulativeFrequency[i];
	}

}

int getChar(std::vector<unsigned long long>& cumulativeFrequency, unsigned long long scaledValue) {

	for (size_t i = 0; i < cumulativeFrequency.size(); ++i) {
		if ( scaledValue < cumulativeFrequency[i + 1] ) {
			return i;
		}

	}

	return 0;

}

probability getProbability(std::vector<unsigned long long>& cumulativeFrequency, int c) {
	probability p = { cumulativeFrequency[c],
					  cumulativeFrequency[c + 1],
					  cumulativeFrequency[cumulativeFrequency.size() - 1]
					};

	updateRanges(cumulativeFrequency, c);

	return p;
}

unsigned long long getCumulative(std::vector<unsigned long long>& cumulativeFrequency) {
	return cumulativeFrequency[cumulativeFrequency.size() - 1];
}

void outputPendingBits(bool bit, std::queue<bool>& buffer, int& pendingBits, std::ofstream& output) {
	buffer.push(bit);
	while ( pendingBits > 0 ) {
		buffer.push( !bit );
		--pendingBits;
	}

	unsigned char c{};
	while (buffer.size() >= 8) {
		c = 0;

		for (int i = 0; i < 8; ++i) {
			c <<= 1;
			c += buffer.front() ? 1 : 0;
			buffer.pop();
		}
	
		output << c;
	}
}

void encode(std::ifstream& input, std::ofstream& output) {
	static const unsigned long long MAX_RANGE { 0xFFFFFFFFU };
	static const unsigned long long THREE_QUARTERS_RANGE { 0xC0000000U };
	static const unsigned long long HALF_RANGE { 0x80000000U };
	static const unsigned long long QUARTER_RANGE { 0x40000000U };

	static const int NUM_OF_SIGNS { 257 };
	static const int EOF_IDX { NUM_OF_SIGNS - 1 };

	unsigned long long high{ MAX_RANGE };
	unsigned long long low{ 0 };
	int rescaleCounter{ 0 };
	std::queue<bool> buffer{};

	char c {};
	probability p{};
	std::vector<unsigned long long> cumulativeFrequency{ initProbability(NUM_OF_SIGNS) };

	unsigned long long range{};
	while ( input.get(c) ) {
		range = high - low + 1;

		p = getProbability(cumulativeFrequency, c);	

		high = low + (range * p.upper / p.cumulative) - 1;
		low = low + (range * p.lower)/p.cumulative;

		for ( ; ; ) {
			if ( high < HALF_RANGE ) {
				outputPendingBits(0, buffer, rescaleCounter, output);	
			} else if ( low >= HALF_RANGE ) {
				outputPendingBits(1, buffer, rescaleCounter, output);
				low -= HALF_RANGE;
				high -= HALF_RANGE;
			} else if ( low >= QUARTER_RANGE && high < THREE_QUARTERS_RANGE ) {
				++rescaleCounter;
				low -= QUARTER_RANGE;
				high -= QUARTER_RANGE;
			} else {
				break;
			}
			low <<= 1;
			high <<= 1;
		}
	}

	// now encode EOF
	range = high - low + 1;
	p = getProbability(cumulativeFrequency, EOF_IDX);

	high = low + (range * p.upper / p.cumulative) - 1;
	low = low + (range * p.lower)/p.cumulative;

	for ( ; ; ) {
			if ( high < HALF_RANGE ) {
				outputPendingBits(0, buffer, rescaleCounter, output);	
			} else if ( low >= HALF_RANGE ) {
				outputPendingBits(1, buffer, rescaleCounter, output);
				low -= HALF_RANGE;
				high -= HALF_RANGE;
			} else if ( low >= QUARTER_RANGE && high < THREE_QUARTERS_RANGE ) {
				++rescaleCounter;
				low -= QUARTER_RANGE;
				high -= QUARTER_RANGE;
			} else {
				break;
			}
			low <<= 1;
			high <<= 1;
		}

	++rescaleCounter;
	if ( low < QUARTER_RANGE ) {
		outputPendingBits(0, buffer, rescaleCounter, output);
	} else {
		outputPendingBits(1, buffer, rescaleCounter, output);
	}
}

int main(int argc, char * argv[]) {
	
	std::ifstream toEncode(argv[1], std::ios::binary | std::ios::ate);
	if (!toEncode.is_open()) {
		return EXIT_FAILURE;
	}
	size_t dataSize = toEncode.tellg();
	toEncode.seekg(0);

	std::ofstream encoded(argv[2], std::ios::binary);

	encode(toEncode, encoded);

	std::cout<< "Compression rate: " << dataSize/(double)encoded.tellp() << '\n';

	toEncode.close();
	encoded.close();

	return 0;
}
