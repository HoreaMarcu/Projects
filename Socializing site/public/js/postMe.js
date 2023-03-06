let params = (new URL(document.location)).searchParams;
let postid = params.get('postid');
let forumid = params.get('forumid');
let userid = params.get('userid');

let url=new URL(document.location)
let index=url.pathname.lastIndexOf('/');
let path =url.pathname.slice(0,index);
let first=true;
document.getElementById("logOutButton").style.display='none';
document.getElementById("logOutButton").addEventListener("click",logOut);



class Post {
    constructor(forum,user,title,date,content,text,upvotes,downvotes){
        this.forum=forum;
        this.user=user;
        this.title=title;
        this.date=date;
        this.content=content;
        this.text=text;
        this.comments=[];
        this.upvotes=upvotes;
        this.downvotes=downvotes;
    }

    addComment(comment){
        this.comments.push(comment);

    }
    upvote(){
        this.upvotes++;
        updatePostToDB(this.title);
        populatePosts();
    }
    downvote(){
        this.downvotes++;
        updatePostToDB(this.title);
        populatePosts();

    }
    

}

class Forum{
    constructor(id,title,moderator,description){
        this.id=id;
        this.title=title;
        this.moderator=moderator;
        this.posts=[];
        this.description=description;

        
    }

    changeDescription(description){
        this.description=description;
    }
    addPost(Post){
        this.posts.push(Post);

    }

}

class Comment{
    constructor(post,user,content,upvotes,downvotes){
        this.post=post;
        this.user=user;
        this.content=content;
        this.upvotes=upvotes;
        this.downvotes=downvotes;
        this.comments=[];
    }

    addComment(comment){
        this.comments.push(comment);

    }
    upvote(){
        this.upvotes++;
        updateCommentToDB(this);
        populatePosts();
    }
    downvote(){
        this.downvotes++;
        updateCommentToDB(this);
        populatePosts();
    }

}
class User{
    constructor(name,password){
        this.name=name;
        this.password=password;

    }
    addComment(message,commnetable,upvotes,downvotes){
        commnetable.addComment(new Comment(commnetable,this,message,upvotes,downvotes));
    }
    addPost(postTitle,content,text,forum){
        var postToAdd=new Post(forum,this.name,postTitle,new Date().toLocaleDateString(),content,text)
        forum.addPost(postToAdd);
        posts.push(postToAdd);
    }
    

}

class RegularUser extends User{
    constructor(name,password){
        super(name,password);
    }

}

class ModeratorUser extends User{
    constructor(name,password){
        super(name,password);
    }

    createForum(forumName,forumDescription){
        id=forums.length;
        forums.push(new Forum(id,forumName,this,forumDescription));

    }

}
document.getElementById("logInButton").addEventListener("click",displayLogInForum);
document.getElementById("cancelLI").addEventListener("click",closeFormLogIn);
document.getElementById("logIn").addEventListener("click",logIn);
if(document.getElementById("addPostButton")!=null){
    document.getElementById("addPostButton").addEventListener("click",displayPostForum);
    document.getElementById("cancelAP").addEventListener("click",closeFormPost);
    document.getElementById("addPost").addEventListener("click",addPost);
}

//document.getElementById("addCommentButton").addEventListener("click",displayCommentForum);
//document.getElementById("cancelC").addEventListener("click",closeFormComment);

forms=document.getElementsByClassName("form");


forms[0].addEventListener('submit', (event) => {
    // stop form submission
    event.preventDefault();
});

forms[1].addEventListener('submit', (event) => {
    // stop form submission
    event.preventDefault();
});
if(document.getElementById("addPostButton")!=null){
    forms[2].addEventListener('submit', (event) => {
        // stop form submission
        event.preventDefault();
    });
}

var postsplace= document.getElementById("posts");
var posts =[];

var forums=[];

var users=[];


let guest=new User("guest","00");
var loggedUser= guest;
users.push(guest);
populateUsers();



var loggedIn;


createForums();
let currentForum=null;
let currentPost=null;
if(forumid!=null){
    currentForum=forums[parseInt(forumid,10)];
    console.log(currentForum);
}
let forumname= document.getElementById('forumname');
if(forumname!=null){
    forumname.innerHTML=currentForum.title;
}
createPosts();
console.log("after")
console.log(posts);




//populatePosts();






logOut();
function logOut(){
    loggedUser=guest;
    loggedIn=false;
    var nameShowed=document.getElementById("shownName");
    nameShowed.innerHTML=guest.name;
    document.getElementById("logOutButton").style.display='none';
    document.getElementById("logInButton").style.display='block';
    document.getElementById("signUpButton").style.display='block';
    getUserString();
    var url1=window.location.href
    console.log(url1);
    if(!first){
        var index1=url1.lastIndexOf('userid=');
        var path1 =url1.slice(0,index1);
        if(index1!=-1){
            location.href=path1+'userid=0';
        }
        populatePosts();
    }
    else
        first=false;
}
//var currentForum=forums[0];







function createForums(){
    let forum1= new Forum(0,"Romania","Gege23","Posts about our beautiful country");
    forums.push(forum1);
    let forum2= new Forum(1,"Games","Halo45","Posts about our favorite games");
    forums.push(forum2);
    let forum3= new Forum(2,"School","Mire11","Posts about how to learn efficiently");
    forums.push(forum3);
}




function createPosts(){
    
    
    //let newPost = new Post(forums[0],"Tre78",'High taxes',"12/12/2012","taxes.jpg","Gouvernment Raises taxes. We're left without food Gouvernment Raises taxes. We're left without foodGouvernment Raises taxes. We're left without food Gouvernment Raises taxes. We're left without foodGouvernment Raises taxes. We're left without food Gouvernment Raises taxes. We're left without food ");
    
    // newPost = new Post(forum1,"Ryo89","Low Pay","10/10/2010","lowpay.jpg","We need more money. We need to feed our families \n   ");
    // posts.push(newPost);
    // forum1.posts.push(newPost);
    // newPost = new Post(forum1,"Geta66","Clean city","09/09/2017","cleancity.jpg","I'm glad our city is this clean. proud to be here");
    // posts.push(newPost);
    // forum1.posts.push(newPost);

    //  newPost = new Post(forum2,"Tr2e78",'High2 taxes',"12/12/2012","taxes.jpg","Gouvernment Raises taxes. We're left without food Gouvernment Raises taxes. We're left without foodGouvernment Raises taxes. We're left without food Gouvernment Raises taxes. We're left without foodGouvernment Raises taxes. We're left without food Gouvernment Raises taxes. We're left without food ");
    // posts.push(newPost);
    // forum2.posts.push(newPost);
    // newPost = new Post(forum2,"Ry2o89","Low2 Pay","10/10/2010","lowpay.jpg","We need more money. We need to feed our families \n   ");
    // posts.push(newPost);
    // forum2.posts.push(newPost);
    // newPost = new Post(forum2,"Get2a66","Clean2 city","09/09/2017","cleancity.jpg","I'm glad our city is this clean. proud to be here");
    // posts.push(newPost);
    // forum2.posts.push(newPost);

    //  newPost = new Post(forum3,"Tre378",'High3 taxes',"12/12/2012","taxes.jpg","Gouvernment Raises taxes. We're left without food Gouvernment Raises taxes. We're left without foodGouvernment Raises taxes. We're left without food Gouvernment Raises taxes. We're left without foodGouvernment Raises taxes. We're left without food Gouvernment Raises taxes. We're left without food ");
    // posts.push(newPost);
    // forum3.posts.push(newPost);
    // newPost = new Post(forum3,"Ry3o89","Low3 Pay","10/10/2010","lowpay.jpg","We need more money. We need to feed our families \n   ");
    // posts.push(newPost);
    // forum3.posts.push(newPost);
    // newPost = new Post(forum3,"Get3a66","Clean3 city","09/09/2017","cleancity.jpg","I'm glad our city is this clean. proud to be here");
    // posts.push(newPost);
    // forum3.posts.push(newPost);
    getPostsFromDB();
    


}

function getUserString(){
    for(i=0;i<users.length;i++){
        if(loggedUser.name==users[i].name){
            
            console.log('&userid='+i)
            return ('&userid='+i);
        }
    }
    console.log("nothing")
    return '';

}
function getUserString2(){
    for(i=0;i<users.length;i++){
        if(loggedUser.name==users[i].name){
            
            console.log('?userid='+i)
            return ('?userid='+i);
        }
    }
    console.log("nothing")
    return '';

}
function populatePosts(){
    populateCategories();
    document.getElementsByClassName("header")[0].innerHTML='<img src="logo.png" height=80px width=100px  onclick="location.href=\'' +path +'/postMe.html'+ getUserString2()+'\';">&nbsp;'
    stringHTML='';
    userString=getUserString();
    console.log("userstring "+userString);
    for(i=0;i<posts.length;i++){
        if(currentPost==null || i==postid){
            stringHTML+='<div class="card" <h2>'+posts[i].title+'<h4 style= "color:grey;"onclick="location.href=\'' +path +'/forum.html?forumid='+posts[i].forum.id+userString +'\';">&nbsp;'+posts[i].forum.title +'</h4></h2><h5>'+posts[i].date
            if (currentPost!=null){
                stringHTML+='<br><br><button class="like" onclick="currentPost.upvote()">'
                stringHTML+='<i class="fa fa-thumbs-o-up" style="font-size:24px" aria-hidden="true" ></i>'
                stringHTML+='</button>'
                stringHTML += "&nbsp;&nbsp;&nbsp;" 
                stringHTML+= currentPost.upvotes;
                stringHTML+="&nbsp;&nbsp;&nbsp;";
                stringHTML+= '<button class="dislike" onclick="currentPost.downvote()">'
                stringHTML+='<i class="fa fa-thumbs-o-down" aria-hidden="true" style="font-size:24px"></i>'
                stringHTML+='</button>'
                stringHTML+="&nbsp;&nbsp;&nbsp;";
                
                stringHTML+= currentPost.downvotes;
                stringHTML+="&nbsp;&nbsp;&nbsp;";
            }
            index=0;
            for(j=0;j<forums.length;j++){
                if(currentForum==forums[j])
                    index=j;
            }

            stringHTML+= '</h5><div class="container1"><div class="fakeimg" style="height:300px;" onclick="location.href=\'' +path +'/post.html?postid='+i+'&forumid='+index+userString+'\';">&nbsp;<img style="width:85% ;width="400" height="250"" src='+posts[i].content + '></div><div class="text" style="float:left;">'+posts[i].text+'</div></div>';
            if(currentPost!=null){
                stringHTML+= '<div class="align-right" style="float:bottom-right; top:-25px">'
                stringHTML+= '<button class="button" id="addCommentButton" style="float:bottom-right; padding:10px; border-radius: 10px" onclick="displayCommentForum()">Add Comment</button>'
                stringHTML+='<div id="id03" class="popUp">'
        
                stringHTML+='<form class="form animate" >'
                stringHTML+=  '<img src="logo.png" height=100px width=120px >'
                stringHTML+=  '<button type="button" class="cancelbtn" id="cancelC" right onclick="closeFormComment()"><i class="fa-solid fa-circle-xmark"></i></button>'
                stringHTML+=  '<div class="container">'
                stringHTML+=   '<label for="Comment" ><b>Add comment</b></label>'
                stringHTML+=  '<input id="addcommenttext" type="text"  placeholder="Enter comment" name="Comment" required>'
            }
            stringHTML+='</div>'
            stringHTML+= '<div class="container" style="background-color:#f1f1f1 ">'        
            stringHTML+=  '</div>'
            stringHTML+= '</form>'
            stringHTML += '</div></div>';

                
                console.log(forms);

                if(currentPost!=null){
                    
                    //currentPost.addComment(new Comment(currentPost,loggedUser,"hehe"));
                    stringHTML+='<br> <div class="fakeimgComm2">' + "<h3> Comments </h3>";
                    for(j=0;j<currentPost.comments.length;j++){
                        stringHTML+='<button class="like" onclick="currentPost.comments[' + j + '].upvote()">'
                        stringHTML+='<i class="fa fa-thumbs-o-up" style="font-size:24px" aria-hidden="true" ></i>'
                        stringHTML+='</button>'

                        stringHTML += "&nbsp;&nbsp;&nbsp;"
                        stringHTML+= currentPost.comments[j].upvotes;
                        stringHTML+="&nbsp;&nbsp;&nbsp;";

                        stringHTML+= '<button class="dislike" style="font-size:24px" onclick="currentPost.comments[' + j + '].downvote()">'
                        stringHTML+='<i class="fa fa-thumbs-o-down" aria-hidden="true" style="font-size:24px"></i>'
                        stringHTML+='</button>'
                        stringHTML+="&nbsp;&nbsp;&nbsp;";

                        stringHTML+= currentPost.comments[j].downvotes;
                        stringHTML+="&nbsp;&nbsp;&nbsp;";   

                        stringHTML+='<br> <div class="fakeimgComm" style="top:-100px; padding: vertical;">';
                        stringHTML+=currentPost.comments[j].user.name +': ';
                        stringHTML+=currentPost.comments[j].content;

                        stringHTML+='</div>';
                    }
                    stringHTML+='</div>'; 



                }
                stringHTML+='</div>'
            //}


            
    
    
    
    
    
    
    
    
        }
    }
    postsplace.innerHTML=stringHTML;
    if(currentPost!=null){
        forms=document.getElementsByClassName("form");
        commentforms=document.getElementsByClassName("addComment");
        j=0;
        
        for(i=0;i<posts.length;i++){
            if(currentPost==null || i==postid){


                commentforms[j].addEventListener("click",addComment);
                if(document.getElementById("addPostButton")!=null){
                    forms[3+j].addEventListener('submit', (event) => {
                        // stop form submission
                        event.preventDefault();
                    });
                }
                else{
                    forms[2+j].addEventListener('submit', (event) => {
                        // stop form submission
                        event.preventDefault();
                    });

                }
                j++;
                
            }
        }
    }
    
}

function populateUsers(){

    //users.push(new RegularUser("regular1","pass"));
    //users.push(new ModeratorUser("moderator1","passm"));
    getUsersFromDB();



}




function addPost(){
    postTitle=document.getElementById("addposttitle").value;
    postDescription=document.getElementById("addpostdescription").value;
    postContent=document.getElementById("addpostcontent").value;
    //loggedUser.addPost(postTitle,postContent,postDescription,currentForum);
    document.getElementById("id00").style.display='none';
    addPostToDB(postTitle,postContent,postDescription);
    getPostsFromDB();
    
    populatePosts();
}

function createForum(){
    forumName=document.getElementById("addforumtitle").value;
    forumDescription= document.getElementById("addforumdescription").value;
    if(loggedUser instanceof ModeratorUser){
        loggedUser.createForum(forumName,forumDescription);
    }
}

function addComment(){
    console.log("addcomm");
    comment=document.getElementById("addcommenttext").value;
    //loggedUser.addComment(comment,currentPost)
    addCommentToDB(comment);
    document.getElementById("id03").style.display='none';
    populatePosts();
    //document.getElementById("commenttextbox").style.display='none';
    //refreshComments();
}


function displayPostForum(){

    document.getElementById("id00").style.display='block';
    console.log("pressed");
}

function closeFormPost(){

    document.getElementById("id00").style.display='none';
    console.log("closed");
}


function displayLogInForum(){

    document.getElementById("id01").style.display='block';
    console.log("pressed");
}

function closeFormLogIn(){

    document.getElementById("id01").style.display='none';
    console.log("closed");
}



/*form.addEventListener('submit', (event) => {
    // stop form submission
    event.preventDefault();
});*/

function logIn(){


    username=document.getElementById("usernameli").value;
    password=document.getElementById("passwordli").value;

    for(i=0;i<users.length;i++){
        if(username==users[i].name){
            if(password==users[i].password){
                loggedUser=users[i];
                console.log("log in succesful" + users[i].name);
                document.getElementById("id01").style.display='none';
                var nameShowed=document.getElementById("shownName");
                nameShowed.innerHTML=username;
                document.getElementById("logOutButton").style.display='block';
                document.getElementById("logInButton").style.display='none';
                document.getElementById("signUpButton").style.display='none';
                

                var url1=window.location.href
                console.log(url1);
                var index1=url1.lastIndexOf('userid=');
                var path1 =url1.slice(0,index1);
                if(index1!=-1){
                    location.href=path1+'userid='+ i;
                }
                else{
                    location.href=path1+'?userid=' +i;
                }
                
                populatePosts();
                return;
            }
        }
    }
    console.log("users:"+users);
    console.log("pass"+password +" name:" + username);
    console.error("log in failed");
    
    

}

function logIn2(username,password){


    //username=document.getElementById("usernameli").value;
    //password=document.getElementById("passwordli").value;

    for(i=0;i<users.length;i++){
        if(username==users[i].name){
            if(password==users[i].password){
                loggedUser=users[i];
                console.log("log in succesful" + users[i].name);
                document.getElementById("id01").style.display='none';
                var nameShowed=document.getElementById("shownName");
                nameShowed.innerHTML=username;
                if(i!=0){
                    document.getElementById("logOutButton").style.display='block';
                    document.getElementById("logInButton").style.display='none';
                    document.getElementById("signUpButton").style.display='none';
                }
                
                // var url1=window.location.href
                // console.log(url1);
                // var index1=url1.lastIndexOf('userid=');
                // var path1 =url1.slice(0,index1);
                // if(index1!=-1){
                //     location.href=path1+'userid='+ i;
                // }
                // else{
                //     location.href=path1+'?userid=' +i;
                // }
                
                populatePosts();
                return;
            }
        }
    }
    console.log("users:"+users);
    console.log("pass"+password +" name:" + username);
    //console.error("log in failed");
    

}


document.getElementById("signUpButton").addEventListener("click",displaySignUpForum);
document.getElementById("cancelSU").addEventListener("click",closeFormSignUp);
document.getElementById("signUp").addEventListener("click",signUp);


function displaySignUpForum(){

    document.getElementById("id02").style.display='block';
    console.log("pressed");
}

function closeFormSignUp(){

    document.getElementById("id02").style.display='none';
    console.log("closed");
}


function signUp(){
//todo
    console.log("signup")

    usernamesu=document.getElementById("usernamesu").value;
    passwordsu=document.getElementById("passwordsu").value;
    email=document.getElementById("emailsu").value;
    addUserToDB(usernamesu,email,passwordsu);
    document.getElementById("id02").style.display='none';
    
    //populateUsers();
    
    

}

function displayCommentForum(){

    document.getElementById("id03").style.display='block';
    console.log("pressed");
}

function closeFormComment(){

    document.getElementById("id03").style.display='none';
    console.log("closed");
}


function addUserToDB(username,email,password){
    let user = {
        name: username,
        email: email,
        password:password
    }

    $.ajax({
        type:'POST',
        url: "/registeruser",
        contentType: "application/json",
        data: JSON.stringify(user),
        success:(data)=>{
            if(data!=""){
                populateUsers();
                //logIn2(username,password);
                //alert(data);
            }
        }
    })

}


function getUsersFromDB(){
    $.ajax({
        type:'GET',
        url: "/findUsers",
        contentType: "application/json",
        dataType: 'json', // added data type
        success: function(res) {
            console.log(res);
            //console.log(res.body);
            users= [];
            for(i=0;i<res.length;i++){
                var name=res[i].name;
                var password=res[i].password;
                users.push(new User(name,password));
            }
            console.log(users);
            //alert(res);
            if(userid!=null){
                let userToBeLogged=users[parseInt(userid,10)];
                logIn2(userToBeLogged.name,userToBeLogged.password);
                console.log(currentForum);
            }
            populatePosts();
            document.getElementsByClassName("header")[0].innerHTML='<img src="logo.png" height=80px width=100px  onclick="location.href=\'' +path +'/postMe.html'+ getUserString2()+'\';">&nbsp;'
            populateCategories();
            document.getElementsByClassName("header")[0].innerHTML='<img src="logo.png" height=80px width=100px  onclick="location.href=\'' +path +'/postMe.html'+ getUserString2()+'\';">&nbsp;'
            if(usernamesu!=null)
                logIn2(usernamesu,passwordsu);
        }
    })

}


function updateCommentToDB(comment){
    let commentAux = {
        content: comment.content,
        user:comment.user.name,
        post:comment.post.title,

        upvotes:comment.upvotes,
        downvotes:comment.downvotes
        //date: new Date().toLocaleDateString()
    }

    $.ajax({
        type:'POST',
        url: "/updatecomment",
        contentType: "application/json",
        data: JSON.stringify(commentAux),
        success:(data)=>{
            if(data!=""){
                alert(data);
            }
        }
    })

}
function addCommentToDB(content){
    let comment = {
        post: currentPost.title,
        user: loggedUser.name,
        content:content
    }

    $.ajax({
        type:'POST',
        url: "/registercomment",
        contentType: "application/json",
        data: JSON.stringify(comment),
        success:(data)=>{
            if(data!=""){
                
                //alert(data);
            }
        }
    })
    getCommentsFromDB();

}


function getCommentsFromDB(){
    $.ajax({
        type:'GET',
        url: "/findComments",
        contentType: "application/json",
        dataType: 'json', // added data type
        success: function(res) {
            console.log(res);
            if(currentPost!=null)currentPost.comments=[];
            for(i=0;i<res.length;i++){
                var post=res[i].post;
                var user=res[i].user;
                var content=res[i].content;
                var upvotes=0;
                if(res[i].upvotes!=null)
                    upvotes=res[i].upvotes;
                var downvotes=0;
                if(res[i].downvotes)
                    downvotes=res[i].downvotes;
                if(post!=null) console.log(post+ " " +currentPost.title);
                if(post!=null && post==currentPost.title)
                    
                    for(j=0;j<users.length;j++){
                        if(users[j].name==user){
                            console.log("found");
                            users[j].addComment(content,currentPost,upvotes,downvotes);
                        }
                    }
            }
            console.log(currentPost.comments);
            populatePosts();
        }
    })

}

function updatePostToDB(title){
    let post = {
        title: title,
        upvotes:currentPost.upvotes,
        downvotes:currentPost.downvotes
        //date: new Date().toLocaleDateString()
    }

    $.ajax({
        type:'POST',
        url: "/updatepost",
        contentType: "application/json",
        data: JSON.stringify(post),
        success:(data)=>{
            if(data!=""){
                alert(data);
            }
        }
    })

}
function addPostToDB(title,content,text){
    let post = {
        title: title,
        content: content,
        text:text,
        forum: currentForum.title,
        upvotes:0,
        downvotes:0
        //date: new Date().toLocaleDateString()
    }

    $.ajax({
        type:'POST',
        url: "/registerpost",
        contentType: "application/json",
        data: JSON.stringify(post),
        success:(data)=>{
            if(data!=""){
                getPostsFromDB();
                //alert(data);
            }
        }
    })

}
function getPostsFromDB(){
    $.ajax({
        type:'GET',
        url: "/findPost",
        contentType: "application/json",
        dataType: 'json', // added data type
        success: function(res) {
            console.log(res);
            //console.log(res.body);
            if(currentForum!=null ) currentForum.posts=[];
            for(i=0;i<res.length;i++){
                var title=res[i].title;
                var content=res[i].content;
                var text = res[i].text;
                var forum = res[i].forum;
                var upvotes=0;
                if(res[i].upvotes!=null)
                    upvotes=res[i].upvotes;
                var downvotes=0;
                if(res[i].downvotes)
                    downvotes=res[i].downvotes;
                if(currentForum!=null && currentForum.title==forum){
                    //loggedUser.addPost(title,content,text,currentForum);
                    console.log("added")
                    
                    
                    currentForum.posts.push(new Post(currentForum,loggedUser,title,new Date().toLocaleDateString(),content,text,upvotes,downvotes));
                    //if(upvotes!=null)
                        
                    
                }
                if(currentForum==null){
                    posts.push(new Post(new Forum(40,"MainPage",loggedUser,"description"),loggedUser,title,new Date().toLocaleDateString(),content,text,upvotes,downvotes));
                }
                
            }
            console.log("The posts are: ");
            


            if(currentForum!=null){
                console.log("here");
                posts=currentForum.posts;
            }
            if(postid!=null&&currentPost==null){
                currentPost=posts[parseInt(postid,10)];
                console.log("int:"+parseInt(postid,10));
                console.log("posts:" + posts);
                console.log(currentForum);
                let postname= document.getElementById('postname');
                if(postname!=null){
                    postname.innerHTML=currentPost.title;
                }
            }
            

            
            populatePosts();
            populateCategories();
            document.getElementsByClassName("header")[0].innerHTML='<img src="logo.png" height=80px width=100px  onclick="location.href=\'' +path +'/postMe.html'+ getUserString2()+'\';">&nbsp;'
            if(currentPost!=null)
                getCommentsFromDB();

            
            //alert(res);
        }
    })

}
//document.getElementsByClassName("header")[0].innerHTML='<img src="logo.png" height=80px width=100px  onclick="location.href=\'' +path +'/postMe.html'+ getUserString2()+'\';">&nbsp;'

//populateCategories();
function populateCategories(){
    var stringHTML='';
    userString=getUserString();
    for(i=0;i<forums.length;i++){
        stringHTML+='<div class=el onclick="location.href=\'' +path +'/forum.html?forumid='+forums[i].id+userString+'\';">&nbsp;'+forums[i].title +'</div>';
    }

    if(document.getElementById("categories")!=null)
        document.getElementById("categories").innerHTML=stringHTML;
}





//onclick="location.href=\'' +path +'/post.html?postid='+i+'\';">&nbsp;