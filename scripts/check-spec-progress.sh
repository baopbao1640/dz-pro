#!/usr/bin/env sh
set -eu

repo_root="$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)"
specs_dir="$repo_root/specs"

if [ ! -d "$specs_dir" ]; then
  exit 0
fi

failed=0

for feature_dir in "$specs_dir"/*; do
  [ -d "$feature_dir" ] || continue

  for required_file in spec.md plan.md tasks.md progress.md; do
    if [ ! -f "$feature_dir/$required_file" ]; then
      echo "Missing $required_file in ${feature_dir#$repo_root/}" >&2
      failed=1
    fi
  done
done

exit "$failed"
