#!/usr/bin/env bash
set -e
SRC="src"
OUT="out"
rm -rf "$OUT"
mkdir -p "$OUT"
echo "Compiling..."
javac -encoding UTF-8 -d "$OUT" -sourcepath "$SRC" "$SRC/com/yahia/ideaflow/app/Main.java"
echo "Running..."
java -cp "$OUT" com.yahia.ideaflow.app.Main
