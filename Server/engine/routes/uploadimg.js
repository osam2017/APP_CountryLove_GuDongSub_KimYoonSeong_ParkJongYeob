var express = require('express');
var path = require('path');
var router = express.Router();
var multiparty = require('multiparty');
var fs = require('fs');


router.post('/', function(req, res, next) {
	var filename=Math.round(Math.random() * Math.pow(2, 50));
	var uploaddir="/uploads/";
      var form = new multiparty.Form();
      // get field name & value
      form.on('field',function(name,value){
           console.log('normal field / name = '+name+' , value = '+value);
      });
      // file upload handling
      form.on('part',function(part){
           var size;
		   var filetype=part.filename.split(".");
		   filetype=filetype[filetype.length - 1 ];
		   //console.log(filetype);
           if (part.filename) {
                 size = part.byteCount;
				 filename+="."+filetype;
           }else{
                 part.resume();
           }
           //console.log("Write Streaming file :"+"./public"+uploaddir+filename);
           var writeStream = fs.createWriteStream("./public"+uploaddir+filename);
           writeStream.filename = filename;

           part.pipe(writeStream);
           part.on('end',function(){
                 writeStream.end();
           });
      });

      // all uploads are completed
      form.on('close',function(){
		  dir={'fileurl' : uploaddir+filename};
           res.status(200).send( JSON.stringify(dir) );
      });

      // track progress
      form.on('progress',function(byteRead,byteExpected){
           console.log(' Reading total  '+byteRead+'/'+byteExpected);
      });

      form.parse(req);

});

 

module.exports = router;