var express = require('express');
var security = require('./security.js');
var sql_info = require('./sql_info.js');

var geocoder = require('geocoder');

var router = express.Router();
var url  = require('url');
var mysql      = require('mysql');
var connection = mysql.createConnection({
	host     : sql_info.host,
	user     : sql_info.id,
	password : sql_info.pw,
	port     : sql_info.port,
	database : sql_info.db
});

function createStringFromTemplate(template, variables) {
    return template.replace(new RegExp("\{([^\{]+)\}", "g"), function(_unused, varName){
        return variables[varName];
    });
}

// GET sql page.

router.get('/', function(req, res) {
	var count=0;
	var classify_tag=""
	query_classify_count = "SELECT * FROM "+sql_info.table_code;
	connection.query(query_classify_count, function(err, rows, fields) {
	  if (!err){
			count=rows.length;
			var tags="<option value='{number}'>{name}</option>"
			for(i=0; i<count; i++){
				tmptag=createStringFromTemplate( tags, {
					number: rows[i].code,
					name: rows[i].classify_name
				});
				//console.log(tmptag);
				classify_tag+=tmptag;
			}
			res.render('append', {
				title: '새로운 혜택',
				classify: classify_tag
			});
		}
	  else{
			console.log('Error while performing Query.', err);
		}
	});
});

router.post('/', function(req, res) {
	var company = req.body.company; //회사 이름
	var detail_addr = req.body.detail_addr; //상세주소
	var info = req.body.info; //간략소개
	var detail_info = req.body.detail_info; //상세소개
	var company_info = req.body.company_info; //회사소개
	var company_tel= req.body.company_tel; //전화번호
	var img_url= req.body.img_url; //이미지 url
	var latitude;//= req.body.latitude; //latitude
	var longitude;//= req.body.longitude; //longitude
	var area=detail_addr;
	var classification=req.body.classification;
	var user_no=0;

	
	geocoder.geocode(detail_addr, function ( err, data ) {
		if(!err){
			
			if(data.status=="OK"){
				if(data.results[0].geometry != undefined){
					latitude=data.results[0].geometry.location.lat;
					longitude=data.results[0].geometry.location.lng;
					
					console.log(latitude, longitude);
					
					query_insertinfo = createStringFromTemplate(
						"INSERT INTO {table}("+
							"company, latitude, longitude, area, classification, info,"+
							"detail_address, detail_info, company_info, company_tel, user_no, img_url"+
						")"+
						"VALUES("+
							"'{company}', {latitude}, {longitude}, '{area}', '{classification}', '{info}',"+
							"'{detail_addr}', '{detail_info}', '{company_info}', '{company_tel}', '{user_no}', '{img_url}'"+
						");",
						{
							table: sql_info.table_info,
							company : company,
							latitude : latitude,
							longitude : longitude,
							area : area,
							classification : classification,
							info : info,
							detail_addr : detail_addr,
							detail_info : detail_info,
							company_info : company_info,
							company_tel : company_tel,
							user_no : user_no,
							img_url : img_url
						}
					);
					
					if(company != "" && detail_addr!= "" && info != "" && detail_info != "" && company_info!= "" && classification!="" && company_tel!=""){
						connection.query(query_insertinfo, function(err, result) {
						  if (!err){
								//console.log('Append success');
								res.send("success");
							}
						  else{
								res.send("failed");
								console.log('Error while performing Query.', err);
							}
						});
					}
					else{
						res.send("notall")
					}
				}
			}
			else{
				res.send("noaddr")
			}
	
		}
		else{
			console.log("Error occur");
		}
	});

});
module.exports = router;