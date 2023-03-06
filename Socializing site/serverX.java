const bodyParser = require('body-parser');
var express = require('express');
const mongodb = require('mongodb');

const app= express();

app.use(bodyParser.json());

const mongoClient = mongodb.MongoClient;
//const url = 'mongodb://localhost:27017/postme';//to change
const url = 'mongodb://127.0.0.1:27017';//to change




app.post('/registeruser',function(req,res){

    mongoClient.connect(url,(err,client)=>{
        if(err){
            res.send('Unable to conect to db ' + err);
            console.log('err');
        }else{
            console.log('Conected to '+ url);
        }
        var db = client.db('postmedb');
        const userCollection = db.collection('user');

        userCollection.find({name:req.body.name}).toArray((err,dbResult)=>{
            if(err){
                res.send('Database query error');
            }else if(dbResult.length){
                res.send('Username already exists in database');
            }else{
                console.log('No records found');
                insertDoc();
            }
        })
        function insertDoc(){
            userCollection.insert(req.body,(err,dbResp)=>{
                if(err){
                    res.send('Unable to insert records ' + err);

                }else{
                    res.send(dbResp.insertedCount + ' doc inserted');
                }
                db.close;
            })
        }


    })


})


app.get('/findUsers',function(req,res){

    mongoClient.connect(url,(err,client)=>{
        if(err){
            res.send('Unable to conect to db ' + err);
            console.log('err');
        }else{
            console.log('Conected to '+ url);
        }
        var db = client.db('postmedb');
        
        db.collection('user').find({}).toArray(function(err, result){
            if(err) throw err
            console.log(result);
            res.send(result);
            db.close;
        })


    })


})


app.post('/registercomment',function(req,res){

    mongoClient.connect(url,(err,client)=>{
        if(err){
            res.send('Unable to conect to db ' + err);
            console.log('err');
        }else{
            console.log('Conected to '+ url);
        }
        var db = client.db('postmedb');
        const userCollection = db.collection('comment');

        insertDoc();
        function insertDoc(){
            userCollection.insert(req.body,(err,dbResp)=>{
                if(err){
                    res.send('Unable to insert records ' + err);

                }else{
                    res.send(dbResp.insertedCount + ' doc inserted');
                }
                db.close;
            })
        }


    })


})

app.post('/updatecomment',function(req,res){

    mongoClient.connect(url,(err,client)=>{
        if(err){
            res.send('Unable to conect to db ' + err);
            console.log('err');
        }else{
            console.log('Conected to '+ url);
        }
        var db = client.db('postmedb');
        const userCollection = db.collection('comment');

        userCollection.updateMany({user:req.body.user,content:req.body.content},{$set:{upvotes:req.body.upvotes,downvotes:req.body.downvotes}},function(err,res){
            if(err) throw err;
            console.log("update succesful");
            db.close;
        })
        
    })
})


app.get('/findComments',function(req,res){

    mongoClient.connect(url,(err,client)=>{
        if(err){
            res.send('Unable to conect to db ' + err);
            console.log('err');
        }else{
            console.log('Conected to '+ url);
        }
        var db = client.db('postmedb');
        
        db.collection('comment').find({}).toArray(function(err, result){
            if(err) throw err
            console.log(result);
            res.send(result);
            db.close;
        })


    })


})


app.post('/registerpost',function(req,res){

    mongoClient.connect(url,(err,client)=>{
        if(err){
            res.send('Unable to conect to db ' + err);
            console.log('err');
        }else{
            console.log('Conected to '+ url);
        }
        var db = client.db('postmedb');
        const userCollection = db.collection('post');

        userCollection.find({title:req.body.title}).toArray((err,dbResult)=>{
            if(err){
                res.send('Database query error');
            }else if(dbResult.length){
                res.send('Username already exists in database');
            }else{
                console.log('No records found');
                insertDoc();
            }
        })
        function insertDoc(){
            userCollection.insert(req.body,(err,dbResp)=>{
                if(err){
                    res.send('Unable to insert records ' + err);

                }else{
                    res.send(dbResp.insertedCount + ' doc inserted');
                }
                db.close;
            })
        }
    })
})

app.post('/updatepost',function(req,res){

    mongoClient.connect(url,(err,client)=>{
        if(err){
            res.send('Unable to conect to db ' + err);
            console.log('err');
        }else{
            console.log('Conected to '+ url);
        }
        var db = client.db('postmedb');
        const userCollection = db.collection('post');

        userCollection.updateOne({title:req.body.title},{$set:{upvotes:req.body.upvotes,downvotes:req.body.downvotes}},function(err,res){
            if(err) throw err;
            console.log("update succesful");
            db.close;
        })
        
    })
})



app.get('/findPost',function(req,res){

    mongoClient.connect(url,(err,client)=>{
        if(err){
            res.send('Unable to conect to db ' + err);
            console.log('err');
        }else{
            console.log('Conected to '+ url);
        }
        var db = client.db('postmedb');
        
        db.collection('post').find({}).toArray(function(err, result){
            if(err) throw err
            console.log(result);
            res.send(result);
            db.close;
        })


    })


})



app.get(['/','/postMe.html'],function(req,res) {
    res.sendFile(__dirname + '/public/postMe.html');
})

app.get(['/post.html'],function(req,res) {
    res.sendFile(__dirname + '/public/post.html');
})

app.get(['/forum.html'],function(req,res) {
    res.sendFile(__dirname + '/public/forum.html');
})

app.get(['/css/postMe.css'],function(req,res) {
    res.sendFile(__dirname + '/public/css/postMe.css');
})

app.get(['/js/postMe.js'],function(req,res) {
    res.sendFile(__dirname + '/public/js/postMe.js');
})


app.get(['/am.png'],function(req,res) {
    res.sendFile(__dirname + '/public/am.png');
})
app.get(['/img0.png'],function(req,res) {
    res.sendFile(__dirname + '/public/img0.png');
})
app.get(['/img1.png'],function(req,res) {
    res.sendFile(__dirname + '/public/img1.png');
})
app.get(['/img2.png'],function(req,res) {
    res.sendFile(__dirname + '/public/img2.png');
})
app.get(['/img3.png'],function(req,res) {
    res.sendFile(__dirname + '/public/img3.png');
})
app.get(['/img4.png'],function(req,res) {
    res.sendFile(__dirname + '/public/img4.png');
})
app.get(['/img5.png'],function(req,res) {
    res.sendFile(__dirname + '/public/img5.png');
})
app.get(['/img6.png'],function(req,res) {
    res.sendFile(__dirname + '/public/img6.png');
})
app.get(['/img7.png'],function(req,res) {
    res.sendFile(__dirname + '/public/img7.png');
})
app.get(['/img8.png'],function(req,res) {
    res.sendFile(__dirname + '/public/img8.png');
})
app.get(['/img9.png'],function(req,res) {
    res.sendFile(__dirname + '/public/img9.png');
})

app.get(['/img0.jpg'],function(req,res) {
    res.sendFile(__dirname + '/public/img0.jpg');
})
app.get(['/img1.jpg'],function(req,res) {
    res.sendFile(__dirname + '/public/img1.jpg');
})
app.get(['/img2.jpg'],function(req,res) {
    res.sendFile(__dirname + '/public/img2.jpg');
})
app.get(['/img3.jpg'],function(req,res) {
    res.sendFile(__dirname + '/public/img3.jpg');
})
app.get(['/img4.jpg'],function(req,res) {
    res.sendFile(__dirname + '/public/img4.jpg');
})
app.get(['/img5.jpg'],function(req,res) {
    res.sendFile(__dirname + '/public/img5.jpg');
})
app.get(['/img6.jpg'],function(req,res) {
    res.sendFile(__dirname + '/public/img6.jpg');
})
app.get(['/img7.jpg'],function(req,res) {
    res.sendFile(__dirname + '/public/img7.jpg');
})
app.get(['/img8.jpg'],function(req,res) {
    res.sendFile(__dirname + '/public/img8.jpg');
})
app.get(['/img9.jpg'],function(req,res) {
    res.sendFile(__dirname + '/public/img9.jpg');
})

app.get(['/logo.png'],function(req,res) {
    res.sendFile(__dirname + '/public/logo.png');
})
app.get(['/ppicture.png'],function(req,res) {
    res.sendFile(__dirname + '/public/ppicture.png');
})
app.get(['/messi.png'],function(req,res) {
    res.sendFile(__dirname + '/public/messi.png');
})
app.get(['/wed.png'],function(req,res) {
    res.sendFile(__dirname + '/public/wed.png');
})
app.get(['/drake.png'],function(req,res) {
    res.sendFile(__dirname + '/public/drake.png');
})
app.get(['/taxes.jpg'],function(req,res) {
    res.sendFile(__dirname + '/public/taxes.jpg');
})
app.get(['/lowpay.jpg'],function(req,res) {
    res.sendFile(__dirname + '/public/lowpay.jpg');
})
app.get(['/cleancity.jpg'],function(req,res) {
    res.sendFile(__dirname + '/public/cleancity.jpg');
})


app.listen(8080,'127.0.0.1',function (){
    console.log('server now listening on port 8080');
});



