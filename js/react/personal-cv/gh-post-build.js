import fs from 'fs'
import { globSync } from 'glob'


const dirs = globSync('./build/**/*.{js,css,html}')

dirs.forEach(path => {
  fs.readFile(path, 'utf8', function (err, data) {
    if (err) {
      return console.log(err);
    }
    var result = data.replace(/\/assets/g, '/road-to-master/assets');

    fs.writeFile(path, result, 'utf8', function (err) {
      if (err) return console.log(err);
    });
  });
})
