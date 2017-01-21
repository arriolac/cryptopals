def hex_to_base64(s):
    return s.decode("hex").encode("base64")

def fixed_xor(s1, s2):
    return ''.join("{0:x}".format(int(i, 16) ^ int(j, 16)) for i,j in zip(s1, s2))


