/* jhs proxy server
 Cookie: handled in node to manage logon
 Expect: (chunk) handled in node and complete result passed to client
 see: ~addons/ide/jhs/node.ijs
*/

var args= process.argv.slice(2);
const nodeport=args[0];const key=args[1];const jhsport=args[2];const breakfile=args[3];const pem=args[4]

const https  = require('https');
const http   = require('http');
const fs     = require('fs');
const crypto = require("crypto");

const bind= '0.0.0.0';  // anybody
const jhshost= "localhost";
const cookiename= "jhs_cookie";
const token=  crypto.randomBytes(16).toString("hex");
const logonhtml= fs.readFileSync(__dirname+'/server.html', 'utf8');

// client reponse with text
function replyx(code,res,p){res.writeHead(code, "OK", {'Content-Type': 'text/html'});res.end(p);}

// client response with cookkie and text
function replyc(code,res,p,cval)
{
  res.writeHead(code, "OK", {'Set-Cookie':cookiename+"="+cval+";Secure;Httponly",'Content-Type': 'text/html'});
  res.end(p);
}

// client response with headers and body string
function replyhb(code,res,p)
{
  res.writeHead(code, "OK", p['headers']);
  res.end(Buffer.from(p['body'],'binary')) // must be buffer to avoid utf8 stuff
}

const options = {
  key: fs.readFileSync(pem+'/key.pem'),
  cert: fs.readFileSync(pem+'/cert.pem')
};

const server = https.createServer(options, (req, res) => {
  var c= get_cookies(req)['jhs_cookie'];
  var url= decodeURIComponent(req.url);
  //  var ip = req.connection.remoteAddress; // could avoid logon for localhost
 
  if(req.method == 'POST')
  {
    dopost(req, res, function() {
      let postdata= decodeURIComponent(req.post)
      let i= postdata.indexOf('?')
      let cmd= postdata.substring(0,i)
      if("jlogon"==cmd) // validate logon key
      {
        if(key==postdata.substring(7)) replyc(200,res,"logon valid",token); else replyc(200,res,"invalid key",0);
      }
      else if(c!=token || "jlogoff"==cmd) // jlogoff from jijx menu ide>logoff
      {
        replyc(403,res,"logon required",0);
      }
      else if("jbreak"==cmd)
      {
       let fd= fs.openSync(breakfile, 'r+');
       let b=new Uint8Array([3])
       fs.readSync(fd,b,0,1,0)
       b[0]= b[0]+1;
       if(b[0]>2)b[0]=2
       fs.writeSync(fd,b,0,1,0); // fd,data,offset,bytes,position
       fs.closeSync(fd);
       replyx(200,res,'break signalled');
      }
      else
      jhsreq("POST",jhshost,jhsport,url,req.post,req,res); // pass to JHS
    });
     return;
  }

  // get
  if(url=="/favicon.ico") // favicon allowed even if not logged on
    jhsreq("GET",jhshost,jhsport,url,"",req,res);
  else if(c!=token)
    replyx(200,res,logonhtml.replace("STATUS",(c!=token)?"logon required":"logon valid"))
  else
    jhsreq("GET",jhshost,jhsport,url,"",req,res);
});

server.listen(nodeport, bind, () => {
  console.log(`JHS node proxy server running at https://${bind}:${nodeport}/`);
});

var get_cookies = function(request) {
  var cookies = {};
  if (typeof(request.headers.cookie) == "undefined") return cookies;
  request.headers && request.headers.cookie.split(';').forEach(function(cookie) {
    var parts = cookie.match(/(.*?)=(.*)$/)
    cookies[ parts[1].trim() ] = (parts[2] || '').trim();
  });
  return cookies;
};

function dopost(req, res, callback) {
  var postdata = "";
      req.on('data', function(data) {
          postdata += data;
          if(postdata.length > 1e6) {
              postdata = "";
              res.writeHead(413, {'Content-Type': 'text/plain'}).end();
              req.connection.destroy();
          }
      });
      req.on('end', function() {
          req.post = postdata;
          callback();
      });
  }

function jhsresponse(res,data){return {'headers': res.headers,'body':data}}

async function jhsreq(gp,host,port,url,body,req,res)
{
 let promise= dorequest(gp,host,port,url,body,req);
 promise.then(good,bad);
 function good(data){replyhb(200,res,data);}
 function bad(data) {replyhb(200,res,data);}
}

function dorequest(gp,host,port,url,body,req){
    let h= req.headers
    h["node-jhs"]= 1
    return new Promise(function(resolve, reject) {
     let options = {hostname: host,port: port,path: url,method: gp, headers: h
    }
    http
      .request(options, res => {
        var data = ""
        res.setEncoding('binary') // critical to get binary data uncompressed
        res.on("data", d => {data += d;})
        res.on("end", () => {resolve(jhsresponse(res,data))})
      })
      .on("error",  (error) => {reject(JSON.stringify(error));})
      .end(body)
  });
}
