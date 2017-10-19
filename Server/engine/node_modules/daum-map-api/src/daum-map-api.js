var api_key = process.env.DAUM_APIKEY

var https_get = function(url, params, callback) {
  var https = require('https')
  var querystring = require('querystring')
  https.get(url + '?' + querystring.stringify(params), function(res) {
    buffer = ''
    res.on('data', function(data) {
      buffer += data
    })
    res.on('error', function(err) {
      console.log(err)
    })
    res.on('end', function() {
      var json = JSON.parse(buffer)
      callback(json)
    })
  })
}


var mapImage = function(query, callback) {
  var url = "https://apis.daum.net/local/v1/search/keyword.json"
  var params = {
    apikey: api_key,
    query: query
  }

  https_get(url, params, function(json) {
    if (json && json.channel) {
      var item = json.channel.item[0]
      if (item) {
        getMapImageLink(item.longitude, item.latitude, callback)
      }
    }
  })
}

var map = function(query, callback) {
  var url = "https://apis.daum.net/local/v1/search/keyword.json"
  var params = {
    apikey: api_key,
    query: query
  }

  https_get(url, params, function(json) {
    if (json && json.channel) {
      var item = json.channel.item[0]
      if (item) {
        var map_link = 'http://map.daum.net/link/map/' + item.id
        if (callback) {
          callback(map_link)
        }
      }
    }
  })
}

var getMapImageLink = function(longitude, latitude, callback) {
  transCoord(longitude, latitude, 'WGS84', 'WCONGNAMUL', function(x, y) {
    var url = 'http://map2.daum.net/map/imageservice'
    var params = {
      IW: 550,
      IH: 350,
      MX: x,
      MY: y,
      CX: x,
      CY: y,
      SCALE: 2.5,
      service: 'open'
    }
    var querystring = require('querystring')
    var image_link = url + '?' + querystring.stringify(params) + '#.png'
    if (callback) {
      callback(image_link)
    }
  })
}

var transCoord = function(x, y, from, to, callback) {
  var url = 'https://apis.daum.net/maps/transcoord'
  var params = {
    apikey: api_key,
    x: x,
    y: y,
    fromCoord: from,
    toCoord: to,
    output: 'json'
  }
  https_get(url, params, function(json) {
    callback(json.x,json.y)
  })
}


module.exports.map = map
module.exports.mapImage = mapImage
