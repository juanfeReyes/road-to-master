import fs from 'fs'
const filePath = './build/client/index.html'
fs.readFile(filePath, 'utf8', function (err,data) {
  if (err) {
    return console.log(err);
  }
  var result = data.replace(/\/assets/g, '/road-to-master/assets');

  fs.writeFile(filePath, result, 'utf8', function (err) {
     if (err) return console.log(err);
  });
});