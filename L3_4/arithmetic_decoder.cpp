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

void decode(std::ifstream& input, std::ofstream& output) {
	static const unsigned long long MAX_RANGE { 0xFFFFFFFFU };
	static const unsigned long long THREE_QUARTERS_RANGE { 0xC0000000U };
	static const unsigned long long HALF_RANGE { 0x80000000U };
	static const unsigned long long QUARTER_RANGE { 0x40000000U };

	static const int NUM_OF_SIGNS { 257 };
	static const int EOF_IDX { NUM_OF_SIGNS - 1 };

	unsigned long long high{ MAX_RANGE };
	unsigned long long low{ 0 };

	unsigned long long value{ 0 };
	unsigned int byte{};

	for (int i = 0; i < 4; ++i) {
		byte = input.get();
		value <<= 8;
		value += byte;	
	}

	int c{};
	std::queue<bool> buffer{};
	probability p{};
	unsigned long long range{}, scaledValue{};
	std::vector<unsigned long long> cumulativeFrequency{ initProbability(NUM_OF_SIGNS) };
	for ( ; ; ) {
		
		range = high - low + 1;
		scaledValue = ((value - low + 1) * getCumulative(cumulativeFrequency) - 1) / range;
		
		c = getChar(cumulativeFrequency, scaledValue);
		p = getProbability(cumulativeFrequency, c);
		
		if (c == EOF_IDX) {
			break;
		}

		output << (char)c;

		high = low + (range * p.upper)/p.cumulative - 1;
      	low = low + (range * p.lower)/p.cumulative;

      	for ( ; ; ) {
      		if ( high < HALF_RANGE ) {
      		
      		} else if ( low >= HALF_RANGE ) {
      			value -= HALF_RANGE;
      			low -= HALF_RANGE;
      			high -= HALF_RANGE;
      		} else if ( low >= QUARTER_RANGE && high < THREE_QUARTERS_RANGE ) {
      			value -= QUARTER_RANGE;
      			low -= QUARTER_RANGE;
      			high -= QUARTER_RANGE;
      		} else if(value == 0) {
      			break;
      		} else {
      			break;
      		}

      		low <<= 1;
      		high <<= 1;
      		value <<= 1;
      		
      		if ( !buffer.empty() ) {
      			value += buffer.front() ? 1 : 0;
      			buffer.pop();
      		} else if(!input.eof()) {
      			byte = input.get();
      			if (input.eof()) {
      				break;
      			}
      		
      			for (int i = 7; i >= 0; --i) {
      				buffer.push((byte >> i) & 1);
      			}

      			value += buffer.front() ? 1 : 0;
      			buffer.pop();
			}
       	}
	}
}


int main(int argc, char * argv[]) {

	std::ifstream toDecode(argv[1], std::ios::binary);
	std::ofstream decoded(argv[2], std::ios::binary);

	if (!toDecode.is_open()) {
		return EXIT_FAILURE;
	}

	decode(toDecode, decoded);

	toDecode.close();
	decoded.close();

	return 0;
}
