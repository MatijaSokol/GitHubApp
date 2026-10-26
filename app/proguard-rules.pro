# Libraries that ship their own consumer rules (Hilt, Compose, OkHttp, Coil, kotlinx.serialization core, ...) are not
# repeated here. Only rules the merged library rules don't already cover live here.

# --- Crash reports -------------------------------------------------------------------------------------------------
# Keep line numbers so release stack traces can be retraced with the mapping file, but hide original file names.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
