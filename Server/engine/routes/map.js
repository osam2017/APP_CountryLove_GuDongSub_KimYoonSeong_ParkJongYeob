var express = require('express');
var daum = require('daum-map-api');
var router = express.Router();


router.get('/:addr', function(req, res) {
	var addr = req.params.addr; // /addr
	daum.mapImage("서울역", function(res){
	  console.log(res);
	});
});

module.exports = router;