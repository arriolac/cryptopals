import crypto_lib

def main():
    print("Secret chat used: '{}'".format(crypto_lib.single_byte_xor_cipher("1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736")))

if __name__ == '__main__':
    main()
