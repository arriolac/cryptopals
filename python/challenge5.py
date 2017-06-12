import crypto_lib

def main():
    key = "ICE"
    input = """Burning 'em, if you ain't quick and nimble
I go crazy when I hear a cymbal"""
    print(crypto_lib.repeating_key_xor(key, input))

if __name__ == '__main__':
    main()
