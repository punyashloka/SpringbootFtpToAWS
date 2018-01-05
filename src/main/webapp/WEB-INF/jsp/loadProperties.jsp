<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>Property Form</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="../css/default.css" />
</head>
<body>
	<form action="proppage" class="register">
	
		<fieldset class="row1">
			<legend>FTP Details </legend>
			<p>
				<label>FTP SERVER BASE URL </label> <input type="text" name="url" class="long"
					required /> <label>FTP SERVER PORT </label> <input type="text" class="long"
					name="port" required />

			</p>
			<p>
				<label>FTP USER NAME </label> <input type="text" name="userName" class="long"
					required /> <label>FTP PASSWORD </label> <input type="password"
					name="passWord" id="password" class="long" required />

				
			</p>
		</fieldset>
		<br />
		<fieldset class="row2">
			<legend>Amazon S3 Details </legend>
			<p>
				<label>ACCESS KEY </label> <input type="text" name="accessKey"
					class="long" required />
			
				<label>SECRET KEY </label> <input type="password" name="secretKey" id="skey"
					class="long" required />
					<br/>
					<!-- <center><button type="button" id="eye2"
					onclick="if(skey.type=='text')skey.type='password'; else skey.type='text';">
					show key</button></center> -->
			</p>
			<p>
				<label>S3 BUCKET NAME </label> <input type="text" name="bucketName"
					class="long" required />
			
			
				<label>AMAZONBASEURL </label> <input type="text" class="long"
					name="amazonBaseUrl" required />
			</p>
			<p>
				<label>AMAZON DOMAIN </label> <input type="text" name="amazonDomain"
					class="long" required />

			
				<label>AMAZON FOLDER NAME </label> <input class="long" type="text"
					name="amazonFolderName" required />

			</p>
		</fieldset>
		
		<fieldset class="row1">
			<legend>Amazon REDSHIFT Information </legend>
			<p>
				<label>RedShiftDbURL </label> <input type="text" class="long"
					name="redshiftDbURL" required />
			
				<label>RedShiftMasterUser name </label> <input type="text"
					 class="long" name="redshiftMasterUsername"
					required />
			</p>
			<p>
				<label>RedShift Master UserPassword </label> <input type="password"
					id="pwd" class="long" name="redshiftMasterUserPassword" required/>

				
			</p>

			
		</fieldset>

		<center>
			<div>
				<button class="button" type="submit" value="send">Submit
					&raquo;</button>
			</div>
		</center>
	</form>
</body>
</html>

