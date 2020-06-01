# List 01 && 02
- Java program that calculates:
    - frequency of each symbol
    - conditional frequency of each symbol after each symbol :D
    - entropy
    - conditional entropy
    
for txt,pdf,doc,mp5,jpg etc. files.

Alphabet:- 8-bit ASCII codes
# List 03 && 04
- Python program that using adaptive arithmetic coding with scaling
    - encodes uncompressed file
    - codes compressed file
    - calculate:
        - entropy
        - average code length
        - compression rate
    
Alphabet:- 8-bit ASCII codes (also accepts symbols that takes more than 8-bit (polish signs))
    
# List 05 && 06
- Java program that using Lempel-Ziv-Welch algorithm to decode and encode files.
- Indexes of dictionary is coded with universal coding,
 it can be coded and decoded with:
    - Fibonacci coding
    - Elias coding
        - Omega 
        - Gamma 
        - Delta
- calculate:
  - entropy
  - average code length
  - compression rate
  - compressed file size
  - uncompressed file size
    

Alphabet:- 8-bit ASCII codes (also accepts symbols that takes more than 8-bit (polish signs))

# List 07 && 08
- Java program for uncompressed images in TGA format to calculate results of coding
 using difference between predictors (8 standards)*
- calculate:
    - code entropy of full image
    - entropy 
        - of output image 
        - of each color component BGR
- program selecting which method* is best for each component and full image
    
# List 09 && 10
- Python program for uncompressed image in TGA format to compute image using
vector quantization of colors. 
- calculate:
     - "mean square error:", mse
     - "signal to noise ratio:", snr
- program using _Linnd-Buz-Gray_ algorithm to get needed quantity colors
# List 11 && 12
- Program decodes and encodes image in TGA format. 
- k ->  bits quantity of quantizer -> quantization
- calculate for output and input image:
     - mean square error
     - signal to noise ratio
- for each color using lowpass and uppass filter that is later
- differential coding
coded  respecitvely differentially and uneven
# List 13 && 14
- 4 subprograms in python:
    - encoder extended Hamming Code(8,4) in->out
    - noiser changes each bit with with probability p to opposed bit
    - decoder extended Hamming Code(8,4) serves info with cases that he found 2 errors
    - checker that checks how many 4- bits blocks are different in 2 inputed files