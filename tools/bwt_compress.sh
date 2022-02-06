# !/bin/bash
cd `dirname $0`/..

FILE_NAME=$1

mysize=$(stat -f%z "$FILE_NAME")
echo "${FILE_NAME} size: ${mysize} bytes"

start_ms=$(ruby -e 'puts (Time.now.to_f * 1000).to_i')

tools/run.sh com.inflaton.datastructures.compression.burrows.BurrowsWheeler - < $FILE_NAME | \
  tools/run.sh com.inflaton.datastructures.compression.burrows.MoveToFront - | \
  tools/run.sh com.inflaton.datastructures.compression.Huffman - | \
  tools/run.sh edu.princeton.cs.algs4.HexDump 64 | tail -1

end_ms=$(ruby -e 'puts (Time.now.to_f * 1000).to_i')
elapsed_ms=$((end_ms - start_ms))
echo "$elapsed_ms ms used"

# tools/bwt_compress.sh src/test/data/burrows/dickens.txt
# src/test/data/burrows/dickens.txt size: 28965453 bytes
# 70379344 bits
# 85478 ms used

# irb(main):001:0> 70379344.0/28965453
# => 2.4297684555459913