
# Remove branches older than 4 weeks
for k in $(git branch | sed /\*/d); do 
  if { [ $k != "main" ] \
    && [ -z "$(git log -1 --before='4 week ago' -s $k)" ] ;} ; then
    git branch -D $k
  fi
done
