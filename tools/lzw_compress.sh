#  !/bin/bash
cd `dirname $0`/..

FILE_NAME=$1

mysize=$(stat -f%z "$FILE_NAME")
echo "${FILE_NAME} size: ${mysize} bytes"

start_ms=$(ruby -e 'puts (Time.now.to_f * 1000).to_i')

# tools/run.sh com.inflaton.datastructures.compression.LZW - < $FILE_NAME | \
#  tools/run.sh edu.princeton.cs.algs4.HexDump 64 | tail -1

tools/run.sh com.inflaton.datastructures.compression.LempelZivWelch - < $FILE_NAME | \
  tools/run.sh edu.princeton.cs.algs4.HexDump 64 | tail -1

end_ms=$(ruby -e 'puts (Time.now.to_f * 1000).to_i')
elapsed_ms=$((end_ms - start_ms))
echo "$elapsed_ms ms used"

# based on improved LZW ==> LempelZivWelch
# tools/lzw_compress.sh src/test/data/burrows/dickens_512K.txt
# src/test/data/burrows/dickens_512K.txt size: 512000 bytes
# 2018344 bits
# 1825 ms used

# based on original LZW of algs4 book
# tools/lzw_compress.sh src/test/data/burrows/dickens_512K.txt
# src/test/data/burrows/dickens_512K.txt size: 512000 bytes
# 2018344 bits
# 4723 ms used

# irb(main):001:0> 2018344.0/512000
# => 3.942078125

# based on improved LZW ==> LempelZivWelch
# algs4 % tools/lzw_compress.sh src/test/data/burrows/dickens.txt     
# src/test/data/burrows/dickens.txt size: 28965453 bytes
# 118099584 bits
# 39963 ms used

# irb(main):002:0> 118099584.0/28965453
# => 4.077256585629785
