var express = require('express');
var security = require('./security.js');
var sql_info = require('./sql_info.js');

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
router.get('/getinfo/:id/:classification', function(req, res) {
	var id = req.params.id; // /sql/getinfo/info_no/classification
	var classification = req.params.classification; // /sql/getinfo/info_no/classification
	
	subquery_getname = createStringFromTemplate(
		"SELECT classify_name FROM {table} WHERE code='{code}'",
		{
			table : sql_info.table_code,
			code : security.sqli(classification)
		}
	);
	query_getinfo = createStringFromTemplate(
		"SELECT id, img_url, company, latitude, longitude, area, ({subquery}) as classification, info, date, detail_address, detail_info, company_info, company_tel"+
		" FROM {table} WHERE id='{id}'",
		{
			table : sql_info.table_info,
			subquery : subquery_getname,
			id:security.sqli(id)
		}
	);
	//console.log(query_getinfo);
	connection.query(query_getinfo, function(err, rows, fields) {
	  if (!err){
	  		//console.log('The solution is: ', rows);
			res.send(JSON.stringify(rows));
		}
	  else{
			console.log('Error while performing Query.', err);
		}
	});
	
});
module.exports = router;



// GET sql page. 
router.get('/getallinfo', function(req, res, next) {
	
	connection.query('SELECT * from '+sql_info.table_info, function(err, rows, fields) {
	  if (!err){
			code_query="SELECT code, classify_name FROM "+sql_info.table_code;
			connection.query(code_query, function(err, datas, fields) {
			  if (!err){
					for(x in rows){
						tmp=rows[x].classification;
						t=0;
						for(y in datas){
							if(datas[y].code==tmp){
								break;
							}
							t+=1;
						}
						//console.log(datas);
						//console.log(tmp);
						rows[x].classification=datas[t].classify_name;
						//console.log(rows[x].classification)
					}
					res.send(JSON.stringify(rows));
				}
			  else{
					console.log('Error while performing Query.', err);
				}
			});
		}
	  else{
			console.log('Error while performing Query.', err);
		}
	});

});
module.exports = router;