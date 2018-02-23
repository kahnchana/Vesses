var express = require('express')
let app = express()

var multer = require('multer');

var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

var path = require('path')
var cmd = require('node-cmd');

//python command
var pycmd='cd  /Users/shehan/Documents/nodejs/PlantCareDoctor/core  && source ~/tensorflow_ml/bin/activate && python label_image.py ';

var storage = multer.diskStorage({
    destination: function (req, file, callback) {
        callback(null, './uploads')
    },
    filename: function (req, file, callback) {
        console.log(file)
        var newname = file.fieldname + '-' + Date.now() + path.extname(file.originalname)
        callback(null, newname) //The Date.now() method returns the number of milliseconds elapsed since 1 January 1970 00:00:00 UTC.
        req.fileUploadedTo=newname
    }
})

app.post('/upload', function (req, res) {
    var upload = multer({
        storage: storage
    }).single('image');
    upload(req, res, function (err) {
        console.log("fileUploadedTo: "+req.fileUploadedTo)
        //Todo: Add code to run the python script 
        /*cmd.get('python label_image.py uploads/'+req.fileUploadedTo, function (err, data, stderr) {
            console.log('Neural Network Response: ', data)
            res.json(formatServerResponse(data));//Todo:formatServerResponse
        });*/
        var pyProcess = cmd.get(pycmd+'../uploads/'+req.fileUploadedTo,function(data, err, stderr) {
            if (!err) {
              //console.log("data from python script " + data)
              console.log('\nML Core Output - data \n\n\n')
              console.log(data)
              res.send(err)
            } else {
              //console.log("python script cmd error: " + err)
              console.log('\nML Core Output - err \n\n\n')
              console.log(err)
              res.send(err)
              }
            }
          );
        //res.sendFile(path.join(__dirname, "example.json"))
    });
})
/*
//This is a test route //TODO:Remove this
app.get('/upload', function (req, res) {
    res.sendFile(path.join(__dirname, "example.json"))
})
*/


var port = process.env.PORT || 8080;

app.listen(port);
console.log('Magic happens at http://localhost:' + port);