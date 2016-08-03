gv={
	allowHashUpdation:true,
	loading:gi('loading'),
	secretCode:readCk('secretCode')||null
};
window.onload=function(){
	initMenu();
	aeh(gi('codeLabel'),'click',function(e){showSect('code',true);});
	//if secret code update it
	if(gv.secretCode)updateCode(gv.secretCode);
	//fire init state
	var initState=getHash();
	if(getQueryStringValue('mode')=='testing')gv.useLocal=true;
	if(!initState)showSect('home',true);
	else showSect(initState,true);
}
function initMenu(){
	var con=gi('menu');
	var dp=[
		{n:'Matches',id:'matches'},
		{n:'Tournaments',id:'tournaments'},
		{n:'Sports',id:'sports'}
	]
	var t1,t2,t3;
	for(var i=0;i<dp.length;i++){
		t1=ce('button');ac(con,t1);t1.className='menuBtn';t1.innerHTML=dp[i].n;aeh(t1,'click',(function(ind){
			return function(){
				showSect(dp[ind].id,true);
			}
		})(i));
	}
}

function showSect(state,allowHashUpdation){
	switch(state){
		case 'home':initHome();break;
		case 'matches':initMatches();break;
		case 'tournaments':initTournaments();break;
		case 'sports':initSports();break;
		case 'code':initCode();break;
		default:showSect('home',true);return;
	}
	if(allowHashUpdation)setHash(state);
}

function initHome(){
	if(!gv.secretCode)return showSect('code',true);
	var sum=gi('summary'),det=gi('details');
	empty(sum);empty(det);det.style.display='none';sum.style.display='';
}
function initCode(){
	var sum=gi('summary'),det=gi('details');
	empty(sum);empty(det);det.style.display='none';sum.style.display='';
	var saveCode=function(){
		updateCode(gi('secretCode').value);
		showSect('home',true);
	}
	var t1,t2,t3;
	t1=ce('div');ac(sum,t1);t1.innerHTML='Update your sportscafe code here';
	t1=ce('input');ac(sum,t1);t1.id='secretCode';aeh(t1,'keyup',function(e){
		if(fkey(e)==13)saveCode();
	})
	t1=ce('button');ac(sum,t1);t1.innerHTML='Save';aeh(t1,'click',saveCode);
}
function initMatches(){
	if(!gv.secretCode)return showSect('code',true);
	var sum=gi('summary'),det=gi('details');
	empty(sum);empty(det);det.style.display='none';sum.style.display='';
	var editMatch=function(d){
		console.log(d);
		sum.style.display='none';det.style.display='';empty(det);
		var form,tourId,matchParties,matchVenue,matchStage,startTime,endTime,questions=[],result;
		var saveMatch=function(){
			if(d&&d.questions)return saveResult(d);
			if(d)return saveQuestions(d);
			//save new match
			form.submit();
			if(qsa('input:invalid').length)return;
			if(tourId.value.indexOf('id=')<0){return alert('Choose sport from dropdown');}
			if(new Date(startTime.value)>new Date(endTime.value)){return alert('Check end time to be > start time');}
			var dp={
				secret_key: "5p0rTsC4f3_9a#e",
				tournament_id: parseInt(tourId.value.split('id=')[1]),
				match_parties: matchParties.value,
				match_venue: matchVenue.value,
				match_stage: matchStage.value,
				match_starttime: new Date(startTime.value).toISOString(),
				match_endtime: new Date(endTime.value).toISOString()
			}
			sendReq('POST','/matches',js(dp),function(rt){
				showMatches();
			});
		}
		var saveQuestions=function(d){
			if(qsa('input:invalid').length)return;
			for(var i=0,qArr=[],q;i<questions.length;i++){
				q={match_id:d.id};
				q.question_text=questions[i].qTxt.value;
				q.question_context=questions[i].qContext.value;
				q.question_option_1=questions[i].qOpt1.value;
				q.question_option_2=questions[i].qOpt2.value;
				q.question_image_1=questions[i].qUrl1.value;
				q.question_image_2=questions[i].qUrl2.value;
				q.question_points=parseInt(questions[i].qPoints.value);
				qArr.push(q);
			}
			var dp={
				secret_key: "5p0rTsC4f3_9a#e",
				questions:qArr
			}
			sendReq('POST','/questions',js(dp),function(rt){
				showMatches();
			});
		};
		var saveResult=function(d){
			if(qsa('input:invalid').length)return;
			for(var i=0,qArr=[],q;i<questions.length;i++){
				q={};
				q.question_id=questions[i].id;
				q.question_answer=parseInt(questions[i].qRes.value);
				qArr.push(q);
			}
			var dp={
				secret_key: "5p0rTsC4f3_9a#e",
				match_id:d.id,
				match_result:result.value,
				questions:qArr
			}
			sendReq('PUT','/matches/answers',js(dp),function(rt){
				showMatches();
			});
		};
		sendReq('POST','/tournaments/all',js({secret_key:gv.secretCode}),function(rt){
			var tours=jp(rt).data;
			var t1,t2,t3,t4,t5;
			t1=ce('div');ac(det,t1);t1.className='sectionTopbar';
				t2=ce('button');ac(t1,t2);t2.innerHTML='Save';aeh(t2,'click',saveMatch);if(d&&d.match_result){t2.style.display='none';}
				t2=ce('button');ac(t1,t2);t2.innerHTML='Cancel/Back';aeh(t2,'click',showMatches);
				t2=ce('div');ac(t1,t2);t2.innerHTML='Add New Match';
			form=t1=ce('form');ac(det,t1);
				t2=ce('div');ac(t1,t2);t2.className='inpCon';
					t3=ce('div');ac(t2,t3);t3.innerHTML='Tournament';
					tourId=t3=ce('input');ac(t2,t3);t3.type='text';t3.setAttribute('list','toursList');t3.required='true';if(d){t3.readOnly='true';t3.value=d.tournament_name+', id='+d.tournament_id;}
					t3=ce('datalist');ac(t2,t3);t3.id='toursList';
						for(var i=0;i<tours.length;i++){
							t4=ce('option');ac(t3,t4);t4.value=tours[i].tournament_name+', id='+tours[i].id;
						}
				t2=ce('div');ac(t1,t2);t2.className='inpCon';
					t3=ce('div');ac(t2,t3);t3.innerHTML='Parties (Eg. RR vs MI, Paes/Bhupathi vs Woodforde/Woodridge)';
					matchParties=t3=ce('input');ac(t2,t3);t3.required='true';if(d){t3.readOnly='true';t3.value=d.match_parties;}
				t2=ce('div');ac(t1,t2);t2.className='inpCon';
					t3=ce('div');ac(t2,t3);t3.innerHTML='Stage (Eg. Match 23, Semifinals)';
					matchStage=t3=ce('input');ac(t2,t3);t3.required='true';if(d){t3.readOnly='true';t3.value=d.match_stage;}
				t2=ce('div');ac(t1,t2);t2.className='inpCon';
					t3=ce('div');ac(t2,t3);t3.innerHTML='Venue (Eg. Bangalore, Wankhede Mumbai)';
					matchVenue=t3=ce('input');ac(t2,t3);t3.required='true';if(d){t3.readOnly='true';t3.value=d.match_venue;}
				t2=ce('div');ac(t1,t2);t2.className='inpCon';
					t3=ce('div');ac(t2,t3);t3.innerHTML='Start Time';
					startTime=t3=ce('input');ac(t2,t3);t3.type='datetime-local';t3.required='true';aeh(t3,'change',function(e){
						// endTime.value=new Date(startTime.value).format('Y-m-d~H:I').replace('~','T');
						var split=new Date(startTime.value).toISOString().split(':');split.pop();endTime.value=split.join(':');
					});if(d){t3.type='text';t3.readOnly='true';t3.value=d.match_starttime;}
				t2=ce('div');ac(t1,t2);t2.className='inpCon';
					t3=ce('div');ac(t2,t3);t3.innerHTML='End Time';
					endTime=t3=ce('input');ac(t2,t3);t3.type='datetime-local';t3.required='true';if(d){t3.type='text';t3.readOnly='true';t3.value=d.match_endtime;}
				if(d){
					var addQuestion=function(con,t){
						t=t||{tid:randomStr(5)};
						//question text
						t4=ce('div');ac(questsCon,t4);t4.className='questCon';
							t.qTxt=t5=ce('input');ac(t4,t5);t5.placeholder='Enter question here';t5.required='true';if(!t.tid){t.qTxt.readOnly='true';t.qTxt.value=t.question_text;}
							t5=ce('div');ac(t4,t5);t5.className='qOptCon';
								t.qOpt1=t6=ce('input');ac(t5,t6);t6.placeholder='Option 1';t6.required='true';if(!t.tid){t.qOpt1.readOnly='true';t.qOpt1.value=t.question_option_1;}
								t.qUrl1=t6=ce('input');ac(t5,t6);t6.placeholder='URL 1';t6.required='true';
									aeh(t6,'change',function(e){t.qImg1.src=t.qUrl1.value;});
									if(!t.tid){t.qUrl1.readOnly='true';t.qUrl1.value=t.question_option_1;}
							t5=ce('div');ac(t4,t5);t5.className='qOptCon';
								t.qOpt2=t6=ce('input');ac(t5,t6);t6.placeholder='Option 2';t6.required='true';if(!t.tid){t.qOpt2.readOnly='true';t.qOpt2.value=t.question_option_2;}
								t.qUrl2=t6=ce('input');ac(t5,t6);t6.placeholder='URL 2';t6.required='true';
									aeh(t6,'change',function(e){t.qImg2.src=t.qUrl2.value;});
									if(!t.tid){t.qUrl2.readOnly='true';t.qUrl2.value=t.question_option_2;};
							t.qContext=t5=ce('input');ac(t4,t5);t5.placeholder='Enter context here';t5.required='true';if(!t.tid){t.qContext.readOnly='true';t.qContext.value=t.question_context;}
							t.qPoints=t5=ce('input');ac(t4,t5);t5.className='qPointsInp';t5.type='number';t5.value=10;if(!t.tid){t.qPoints.readOnly='true';t.qPoints.value=t.question_points;}
							t.qImg1=t5=ce('img');ac(t4,t5);t5.className='qImg1';
							t.qImg2=t5=ce('img');ac(t4,t5);t5.className='qImg2';
							if(d.questions){
								t.qRes=t5=ce('select');ac(t4,t5);t5.className='qResInp';
									t6=ce('option');ac(t5,t6);t6.value='1';t6.innerHTML=t.qOpt1.value;
									t6=ce('option');ac(t5,t6);t6.value='2';t6.innerHTML=t.qOpt2.value;
									t6=ce('option');ac(t5,t6);t6.value='0';t6.innerHTML='Both/Neither';
								if(t.question_answer){t.qRes.readOnly='true';t.qRes.value=(""+t.question_answer);};
							}
							if(!d.questions){
								t5=ce('button');ac(t4,t5);t5.className='negBtn questDelBtn';t5.innerHTML='Remove';aeh(t5,'click',(function(qtid){
									return function(e){
										del(gparent(getEvtSrc(e)));
										questions=questions.filter(function(d){console.log(qtid,d.tid);return d.tid!=qtid;});
									}
								})(t.tid));
							}
						//option 1, option 2
						//url 1, url2
						questions.push(t);
					}
					t2=ce('div');ac(t1,t2);t2.className='resCon';
						t3=ce('div');ac(t2,t3);t3.className='resTit';t3.innerHTML='Questions';
						var questsCon=t3=ce('div');ac(t2,t3);t3.className='questsCon';
						if(d.questions){
							//populate the questions
							for(var i=0;i<d.questions.length;i++)addQuestion(questsCon,d.questions[i]);
							result=t3=ce('input');ac(t2,t3);t3.id='resultInput';t3.placeholder='Result here';t3.required='true';if(d.match_result){t3.readOnly='true';t3.value=d.match_result;}
						}else{
							t3=ce('button');ac(t2,t3);t3.className='negBtn';t3.innerHTML='Add a question';aeh(t3,'click',function(e){
								addQuestion(questsCon);
							});
						}
				}
		});
	}
	var showMatches=function(){
		sum.style.display='';det.style.display='none';empty(sum);
		var t1,t2,t3;
		t1=ce('div');ac(sum,t1);t1.className='sectionTopbar';
			t2=ce('button');ac(t1,t2);t2.innerHTML='Add';aeh(t2,'click',function(e){editMatch();});
			t2=ce('div');ac(t1,t2);t2.innerHTML='Matches';
		sendReq('POST','/matches/all',js({secret_key:gv.secretCode}),function(rt){
			var matches=jp(rt).data;
			console.log(matches);
			for(var i=0;i<matches.length;i++){
				matches[i].match_starttime=new Date(matches[i].match_starttime).format('d M Y H:I:s A');
				matches[i].match_endtime=new Date(matches[i].match_endtime).format('d M Y H:I:s A');
				// matches[i].match_result=matches[i].match_result||'-';
				matches[i].numQuestions=matches[i].questions?matches[i].questions.length:0;
			}
			//create table
			var tbl=new BasicTbl({con:sum,dp:matches,dpf:[{tit:'Parties',key:'match_parties'},{tit:'Tournament',key:'tournament_name'},{tit:'Time',key:'match_endtime'},{tit:'Stage',key:'match_venue'},{tit:'Questions',key:'numQuestions'},{tit:'Result',key:'match_result'}],onRowClick:function(e,data){
				editMatch(data);
			}});
		});
	}
	showMatches();
}
function initTournaments(){
	if(!gv.secretCode)return showSect('code',true);
	var sum=gi('summary'),det=gi('details');
	empty(sum);empty(det);det.style.display='none';sum.style.display='';
	var editTournament=function(d){
		sum.style.display='none';det.style.display='';empty(det);
		var form,sportId,name,startDt,endDt;
		var saveTournament=function(){
			form.submit();
			if(qsa('input:invalid').length)return;
			if(sportId.value.indexOf('id=')<0){return alert('Choose sport from dropdown');}
			if(new Date(startDt.value)>new Date(endDt.value)){return alert('Check end date to be > start date');}
			var dp={
				secret_key: "5p0rTsC4f3_9a#e",
				sports_id: parseInt(sportId.value.split('id=')[1]),
				tournament_name: name.value,
				tournament_startdate: new Date(startDt.value).toISOString(),
				tournament_enddate: new Date(endDt.value).toISOString()
			}
			sendReq('POST','/tournaments',js(dp),function(rt){
				showTournaments();
			});
		}
		sendReq('POST','/sports/all',js({secret_key:gv.secretCode}),function(rt){
			var sports=jp(rt).data;
			var t1,t2,t3;
			t1=ce('div');ac(det,t1);t1.className='sectionTopbar';
				t2=ce('button');ac(t1,t2);t2.innerHTML='Save';aeh(t2,'click',saveTournament);
				t2=ce('button');ac(t1,t2);t2.innerHTML='Cancel';aeh(t2,'click',showTournaments);
				t2=ce('div');ac(t1,t2);t2.innerHTML='Add New Tournament';
			form=t1=ce('form');ac(det,t1);
				t2=ce('div');ac(t1,t2);t2.className='inpCon';
					t3=ce('div');ac(t2,t3);t3.innerHTML='Sport';
					sportId=t3=ce('input');ac(t2,t3);t3.type='text';t3.setAttribute('list','sportsList');t3.required='true';
					t3=ce('datalist');ac(t2,t3);t3.id='sportsList';
						for(var i=0;i<sports.length;i++){
							t4=ce('option');ac(t3,t4);t4.value=sports[i].sports_name+', id='+sports[i].id;
						}
				t2=ce('div');ac(t1,t2);t2.className='inpCon';
					t3=ce('div');ac(t2,t3);t3.innerHTML='Name';
					name=t3=ce('input');ac(t2,t3);t3.required='true';
				t2=ce('div');ac(t1,t2);t2.className='inpCon';
					t3=ce('div');ac(t2,t3);t3.innerHTML='Start Date';
					 startDt=t3=ce('input');ac(t2,t3);t3.type='date';t3.required='true';aeh(t3,'change',function(e){endDt.value=new Date(startDt.value).format('Y-m-d');});
				t2=ce('div');ac(t1,t2);t2.className='inpCon';
					t3=ce('div');ac(t2,t3);t3.innerHTML='End Date';
					endDt=t3=ce('input');ac(t2,t3);t3.type='date';t3.required='true';
		});
	}
	var showTournaments=function(){
		sum.style.display='';det.style.display='none';empty(sum);
		var t1,t2,t3;
		t1=ce('div');ac(sum,t1);t1.className='sectionTopbar';
			t2=ce('button');ac(t1,t2);t2.innerHTML='Add';aeh(t2,'click',function(e){editTournament();});
			t2=ce('div');ac(t1,t2);t2.innerHTML='Tournaments';
		sendReq('POST','/tournaments/all',js({secret_key:gv.secretCode}),function(rt){
			var tours=jp(rt).data;
			for(var i=0;i<tours.length;i++){
				tours[i].tournament_startdate=new Date(tours[i].tournament_startdate).format('d M Y H:I:s A');
				tours[i].tournament_enddate=new Date(tours[i].tournament_enddate).format('d M Y H:I:s A');
			}
			//create table
			var tbl=new BasicTbl({con:sum,dp:tours,dpf:[{tit:'Tour ID',key:'id'},{tit:'Tour Name',key:'tournament_name'},{tit:'Sport Name',key:'sports_name'},{tit:'Start Date',key:'tournament_startdate'},{tit:'End Date',key:'tournament_enddate'}],onRowClick:function(e,data){
			}});
		});
	}
	showTournaments();
}
function initSports(){
	if(!gv.secretCode)return showSect('code',true);
	var sum=gi('summary'),det=gi('details');
	empty(sum);empty(det);det.style.display='none';sum.style.display='';
	//start
	var showSports=function(){
		var t1,t2,t3;
		t1=ce('div');ac(sum,t1);t1.className='sectionTit';t1.innerHTML='Sports';
		sendReq('POST','/sports/all',js({secret_key:gv.secretCode}),function(rt){
			var sports=jp(rt).data;
			//create table
			var tbl=new BasicTbl({con:sum,dp:sports,dpf:[{tit:'Sport ID',key:'id'},{tit:'Sport Name',key:'sports_name'}],onRowClick:function(e,data){
			}});
		});
	}
	showSports();
}
//utilities
var sendReq=function(method,url,payload,cb){
	var urlRoot=gv.useLocal?'http://localhost:4020/v1/game':'https://api-stage01.sportscafe.in/v1/game';
	var x=new XMLHttpRequest();x.open(method,urlRoot+url);x.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	x.onreadystatechange=function(){if(x.readyState==4){if(x.status==401){showSect('code',true);unWait();alert('Unauthorized - check your code');return;}if(x.status==200){var rt=x.responseText;if(!jp(rt).data){unWait();return alert('An error occurred. Try again and contact admin if it persists.');}
		unWait();
		cb(rt);
	}}};
	x.send(payload);wait();
}
function updateCode(code){
	gv.secretCode=code;
	writeCk('secretCode',gv.secretCode,0.1);
	gi('codeLabel').innerHTML='Your code is <strong>'+code+'</strong>';
}
function wait(){
	gv.loading.style.display='';
}
function unWait(){
	gv.loading.style.display='none';
}
function getQueryStringValue (key) {
 	return unescape(window.location.search.replace(new RegExp("^(?:.*[&\\?]" + escape(key).replace(/[\.\+\*]/g, "\\$&") + "(?:\\=([^&]*))?)?.*$", "i"), "$1"));
}
function BasicTbl(o){
	var con=o.con,table;
	this.createTbl=function(){
		var t1,t2,t3;
		table=t1=ce('table');ac(con,t1);
			t2=ce('tr');ac(t1,t2);
				for(var i=0;i<o.dpf.length;i++){
					t3=ce('th');ac(t2,t3);t3.innerHTML=o.dpf[i].tit;
				}
	};
	this.populateRows=function(){
		if(!table)return;
		var t1,t2,t3;
		for(var i=0;i<o.dp.length;i++){
			t1=ce('tr');ac(table,t1);aeh(t1,'click',(function(data){
				return function(e){
					o.onRowClick(e,data);
				};
			})(o.dp[i]));
				for(var j=0;j<o.dpf.length;j++){
					t2=ce('td');ac(t1,t2);t2.innerHTML=o.dp[i][o.dpf[j].key];
				}
		}
	}
	this.createTbl();
	this.populateRows();
};