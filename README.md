# road-to-master
Repository to experiment and become expert on multiple areas

## Mono Repo migration

```sh
cd repo
git filter-repo  --to-subdirectory-filter "subdir-path" --tag-rename '':'repo_name-'

cd monorepo_path
git remote add -f temp repo_path //first-time
git remote set-url -f gachou-web-ui repo_path
git fetch temp
git merge -m "integrating repo_name" temp/main --allow-unrelated-histories
```
