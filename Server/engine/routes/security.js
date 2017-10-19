exports.sqli = function(string){
	return string.replace("\\", "\\\\").replace("'", "\\'").replace("\"", "\\\"");
};