import base64

def hex_to_base64(s):
    return base64.b64encode(bytes.fromhex(s))

def fixed_xor(s1, s2):
    return ''.join(hex(int(i, 16) ^ int(j, 16)) for i,j in zip(s1, s2))

def hex(x):
    return "{0:x}".format(x)

def single_byte_xor_cipher(s):
    max = 0
    str = s
    for i in range(256):
        hex_val = hex(i)
        other_str = hex_val * int((len(s) / len(hex_val)))
        decrypted_string_hex = fixed_xor(s, other_str)
        decrypted_string = bytes.fromhex(decrypted_string_hex)
        score = string_score(decrypted_string)
        if score > max:
            max = score
            key = chr(i)
            str = decrypted_string
    print(str)
    return key

def string_score(s):
    score = 0
    for ch in s:
        c = chr(ch)
        if c.isalpha() or c == ' ':
            score += 1
    return score

def repeating_key_xor(key, text):
    repeated_key = key * int((len(text) / len(key)) + 1)
    return ''.join(hex(ord(i) ^ ord(j)) for i, j in zip(text, repeated_key))

