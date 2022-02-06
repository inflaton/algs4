#  !/bin/bash
cd `dirname $0`/..

FILE_NAME=$1

mysize=$(stat -f%z "$FILE_NAME")
echo "${FILE_NAME} size: ${mysize} bytes"

start_ms=$(ruby -e 'puts (Time.now.to_f * 1000).to_i')

tools/run.sh com.inflaton.datastructures.compression.Huffman - < $FILE_NAME | \
  tools/run.sh edu.princeton.cs.algs4.HexDump 64 | tail -1

end_ms=$(ruby -e 'puts (Time.now.to_f * 1000).to_i')
elapsed_ms=$((end_ms - start_ms))
echo "$elapsed_ms ms used"

# tools/huf_compress.sh src/test/data/burrows/dickens.txt  
# src/test/data/burrows/dickens.txt size: 28965453 bytes
# 131055944 bits
# 45826 ms used

# irb(main):005:0> 131055944.0/28965453
# => 4.524560482447832
