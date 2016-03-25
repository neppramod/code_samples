<!-- A jsp file that uploads image, converts it using appropriate profile, saves image retrival attributes to database and so on -->
<%@page import="...util.UniqueId"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="java.util.regex.Pattern"%>
<%@page import="java.awt.Rectangle"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.io.File"%>
<%@ page import="java.io.IOException"%>
<%@ page import="...util.SimpleImageInfo"%>
<%@ page import="javax.imageio.ImageReader"%>
<%@ page import="javax.imageio.stream.FileImageInputStream"%>
<%@ page import="javax.imageio.stream.ImageInputStream"%>
<%@ page import="magick.*"%>
<%@ page import="...util.CookieManager" %>
<%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@ page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@ page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@ page import="org.apache.commons.fileupload.FileUploadException"%>
<%@ page import="org.apache.commons.fileupload.FileItem"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="static ...util.PrimaryKeyGeneratorManager.getKey" %>
<%@ page import="java.io.OutputStream"%>
<%@ page import="java.io.FileOutputStream"%>
<%@ page import="java.io.FileInputStream"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.PreparedStatement"%>
<%@ page import="com.mysql.jdbc.Statement"%>
<%@ page import="org.apache.log4j.Logger"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="...util.*"%>
<%@ page
	import="static ...util.ConvertImage.resizeImageWithHint"%>
<%@ page import="static ...util.ConvertImage.resizeImage"%>
<%@ page import="static ...util.ImageSizes.*"%>
<%@ page import="static ...util.Paths.*"%>
<%@ page import="static ...util.Utils.*"%>
<%@ page import="static ...util.Scale.*"%>

<%@ page import="...progressbar.FileUploadListener"%>
<%@ page
	import="...progressbar.FileUploadListener.FileUploadStats"%>
<%@ page
	import="org.jrest4guice.commons.fileupload.MonitoredDiskFileItemFactory"%>

<%@ page import="...util.DatabaseInfo"%>
<%@ page import="...UpdateSelectedProjectId"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>After Upload</title>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/dojo/dojo.js.uncompressed.js"></script>
<!--  <script type="text/javascript" src="application.js"></script> -->
<script type="text/javascript" src="js/dnd.js"></script>
<style>/*
body, td, contentDiv { font-family:arial;font-size:14px; }
h1 { font-size:16px; font-weight:bold; }
.error { color:red;font-weight:bold;}

 
body {
	font-family: "Lucida Grande", Arial, Verdana, Helvetica, sans-serif;
	background-color: #ced1d5;
}
*/
#wrapper {
	margin-right: auto;
	margin-left: auto;
	position: relative;
	height: 100%;
	display: table;
}
#wrapper #goods {
	display:table-cell;
	vertical-align:middle;
	position:relative;
	height:100%;
	white-space: nowrap;
}
.notes {
	font-size: 10px;
	font-style: normal;
	font-weight: normal;
	color: #7E7E7E;
	margin-top: 8px;
}
.myLabel   {
	font-size: 12px;
	font-weight: bold;
	color: #565759;
	margin-top: 30px;
	margin-bottom: 10px;
}
#ProjectName {
	width: 200px;
}
.cloud {
	margin-bottom: 12px;
	height: 58px;
	width: 88px;
	position: relative;
	display: block;
	margin-right: auto;
	margin-left: auto;
}
#snow {
	-moz-border-radius: 4px;
	border-radius: 4px;
	-webkit-box-shadow: inset 0px 1px 3px rgba(0, 0, 0, 0.3), 0px 1px 1px white;
	-moz-box-shadow: inset 0px 1px 3px rgba(0, 0, 0, 0.3), 0px 1px 1px white;
	background-image: url(../img/snowBg.png);
	background-repeat: repeat;
	padding: 20px;
	width: 300px;
	height: 200px;
	overflow-y: scroll;
}

@charset "UTF-8";
/* CSS Document */

html {
	height: 100%;
}
area {
	outline:none;
}
body {
	height: 100%;
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	overflow: hidden;
	overflow-x: auto;
	overflow-y: auto;

}
body, td, th {
	font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
	font-size: 11px;
	color: #333333;
	text-align: left;
	/*border-bottom: 1px solid #111111;*/
}

#content {
	position: relative;
	margin-top: 0px;
	margin-right: 0px auto;
	margin-left: 0px auto;
	z-index: 90;
	min-width: 982px;
	display: inherit;
	height: 100%;
	overflow: hidden;
}
span{
	font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
	font-size: 14px;
	color: #333333;
}
h1  {
	font-size: 14px;
	font-weight: bold;
	color: #222;
	padding: 0px;
	text-shadow: 0px 1px 1px #fff;
	margin-right: 0px;
	margin-left: 0px;
	margin-top:16px;
	text-align: center;
	line-height: 16px;
}
h2{
	font-family:"Helvetica Neue", Helvetica, Arial, sans-serif;
	font-size: 10px;
	font-style: normal;
	line-height: normal;
	font-weight: normal;
	padding: 0px;
	color: #8D8D8D;
	margin-top: 4px;
	margin-right: 0px;
	margin-bottom: 21px;
	margin-left: 0px;
	text-shadow: 0px 1px 1px white;
	text-align: center;

}
.st {
    color: #8D8D8D;
    font-size: 10px;
    font-style: normal;
    font-weight: normal;
    line-height: normal;
    margin: 4px 0 21px;
    padding: 0;
    text-align: center;
    text-shadow: 0 1px 1px #FFFFFF;
}

</style>

<script>
function refreshParent(){
	parent.document.getElementById('upload').disabled=true;
		if(parent.document.getElementById('files').files!=''){
			if(parent.document.getElementById('selectmenuid').value!="All Images")
				parent.loadSideList(parent.document.getElementById('selectmenuid').value);
			else
				parent.loadSideList();
		parent.document.getElementById('files').value= '';
		parent.document.upload_images.action = '';
		parent.document.getElementById('upload').disabled=true;
		parent.document.getElementById('progressbar').innerHTML = parent.document.getElementById('progressbarbackup').innerHTML
		parent.document.getElementById('projectName').value="New Project Name";
		parent.document.getElementById('projectName').style.color="#ccc";
		parent.window.opener.location.reload();
	}
	else{
		parent.document.getElementById('files').value= '';
		parent.document.getElementById('upload').disabled=true;

	}
	
}
</script>
</head>
<body onload="refreshParent();">
	<%
	//Cookie check
	CookieManager cookieManager = new CookieManager();
	
	cookieManager.checkCookie(request, response);
	Integer loginId = cookieManager.getUserValue();

	if (loginId == null) {
		return;
	}

	
	final Logger log = Logger.getLogger("uploadpimagick.jsp");
	SimpleImageInfo imageInfo;
	//Integer USERID = Integer.parseInt(session.getAttribute("userids").toString());
	Integer USERID = cookieManager.getUserValue();
	log.error("useriid in uploadpmagic " + USERID);
	Integer spId = 0;
	UserLoginInfo userLoginInfo = new UserLoginInfo();
	userLoginInfo.setTypeId(loginId + "");
	if(loginId != null) {
		spId = userLoginInfo.getSpid();
		log.error("sppppidddddd::::"+spId);
	}
	
     double startTime;
	 double endTime;
	 double spendTime;
	 String spendTimes="";
	 String dwnspeedandsize="";
	 String[] dwnsize = null;
	 String sizes = "";
	 String timenumberofitems = "";
	 String[] timeitem = null;
	 String info = "";
	
	if (USERID != null) {
	
		long UNIX_TIMESTAMP = System.currentTimeMillis();
		
		String projectId = request.getParameter("selectmenuids");
		log.error("pr::"+projectId);

		if(projectId == null || projectId == "" || projectId=="All Images" || projectId.equals("All Images")){
			projectId = "il";		
		}
		else if(projectId=="New Project" || projectId.equals("New Project")){
			
			if(session.getAttribute("projectIdd")!=null){
				projectId = session.getAttribute("projectIdd").toString();	     		
			}
			else{
				projectId = spId+"";
			}
		}
		else{
			projectId = projectId.substring(1);
		}

		log.error("Project Id: " + projectId);

		String imageIds = "";
		int numofitems=0;
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart) {
		} else {
			
			String saveFilePath = TMP_DIR;
			//String saveFilePath = IMAGES_DIR;
            FileUploadListener listener = new FileUploadListener(request.getContentLength());
           request.getSession().setAttribute("FILE_UPLOAD_STATS", listener.getFileUploadStats());
            DiskFileItemFactory factory = new MonitoredDiskFileItemFactory(listener);
            factory.setRepository(new File(saveFilePath));
            ServletFileUpload upload = new ServletFileUpload(factory);
			List items = null;
			try {
				startTime = System.currentTimeMillis();
				items = upload.parseRequest(request);	
				endTime = System.currentTimeMillis();
				spendTime = (endTime - startTime)/1000;	
				if(Math.round(spendTime)<1)
					spendTimes = 1 + "";
				else
					spendTimes = Math.round(spendTime) + "";
				
				//session.setAttribute("timespend",spendTimes);
				//session.setAttribute("list1",items);a
			} catch (FileUploadException e) {
				log.error("Could not parse upload request");
				log.error(e.getMessage());
			}

			Iterator itr = items.iterator();
	
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				String UNIQUE_ID = new UniqueId().getUniqueId();

				if (item.isFormField()) {
				} else {
					try {
						numofitems++;
						final String itemName = item.getName();
						
						
						if (" ".equals(itemName))
							continue;
						
						Pattern pat = Pattern.compile("\\s+");
						Matcher mat = pat.matcher(itemName);
						
						String replacedFileName = "";
						
						while (mat.find()) {
							replacedFileName += mat.replaceAll("_");
						}
						
						if (replacedFileName == "")
							replacedFileName = itemName;
						
						
						String original_name = StringUtils.substringBeforeLast(replacedFileName, ".");
						String file_name = UNIQUE_ID + FILENAME_SEPARATOR + IMAGE_FORMAT;
						

						final InputStream is = (FileInputStream) item
								.getInputStream();
						final OutputStream output = new FileOutputStream(
								new File(TMP_DIR + replacedFileName));

						int read = 0;
						byte[] bytes = new byte[1024];

						while ((read = is.read(bytes)) != -1)
							output.write(bytes, 0, read);

						is.close();
						output.flush();
						output.close();

						log.info("New file created");

						// Check if the directory exists for current user else create
						File parentdir = new File(UPLOAD_DIR + USERID);

						if (!parentdir.exists()) {
							boolean created = (new File(UPLOAD_DIR + USERID))
									.mkdirs();

							if (!created) {
								out.println("Could not create directory for user");
							}
						}

						// Insert Image information into database
						Connection conn = new DatabaseInfo()
								.getConnection();

						try {
							PreparedStatement stmt = conn
									.prepareStatement("INSERT INTO  Image (original_name, user_table_id, status, file_name, date_added) values (?, ?, " + INITIAL_IMAGE_STATUS +", ?, NOW())", Statement.RETURN_GENERATED_KEYS);

							stmt.setString(1, original_name);
							stmt.setInt(2, USERID);
							stmt.setString(3, file_name);
							
							int affectedRows = stmt.executeUpdate();
							log.error("affectedRowss:::"+affectedRows);
							if (affectedRows == 0)
								throw new SQLException("Saving Image Failed !!. Sorry");
								
							ResultSet generatedKeys = stmt.getGeneratedKeys();
							int lastInsertedId = -1;
							
							
							// Lets prepare for insertion in ProjectImage table
							PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO ProjectImage (project_id, image_id, date_added, project_image_id) VALUES (?, ?, NOW(), ?)");
							
							if (generatedKeys != null && generatedKeys.next()) {
								lastInsertedId = generatedKeys.getInt(1);
								imageIds += lastInsertedId + IMAGE_DELIMETER;
								log.error("PPPP"+imageIds);
								if (StringUtils.isNumeric(projectId)) {
									log.error("!QQQQ");
									stmt1.setInt(1, Integer.parseInt(projectId));
									stmt1.setInt(2, lastInsertedId);
									stmt1.setInt(3, getKey(PRIMARY_PROJECT_IMAGE));
									stmt1.execute();
								}
							}
							if(projectId=="il" || projectId.equals("") || projectId==""){
		
						
								if(UpdateSelectedProjectId.setSelectedProjectId(conn, 0, USERID)>0)
								 log.error("Sucessfully updated selectedProjectId in UserTable for All Images");
							}
							
							
							
							
							

						} catch (SQLException ex) {
							out.println("Oops!!. Sorry my fault!! Visit next time or report an error<br/>");
							log.error(ex.getMessage());
							return;
						} finally {
							conn.close();
						}

						// Check if individual dirs exists for each photo else create
						File photodir = null;

						// Single view mode directory check
						photodir = new File(UPLOAD_DIR + USERID
								+ SINGLE_DIR);

						if (!photodir.exists()) {
							boolean created = (new File(UPLOAD_DIR + USERID
									+ SINGLE_DIR)).mkdir();

							if (!created) {
								out.println("Could not create single view directory for user ");
							}
						}

						// Spread view mode directory check
						photodir = new File(UPLOAD_DIR + USERID
								+ SPREAD_DIR);

						if (!photodir.exists()) {
							boolean created = (new File(UPLOAD_DIR + USERID
									+ SPREAD_DIR)).mkdir();

							if (!created) {
								out.println("Could not create spread view directory for user ");
							}
						}

						// Thumbnail view mode directory check
						photodir = new File(UPLOAD_DIR + USERID
								+ THUMBNAIL_DIR);

						if (!photodir.exists()) {
							boolean created = (new File(UPLOAD_DIR + USERID
									+ THUMBNAIL_DIR)).mkdir();

							if (!created) {
								out.println("Could not create thumbnail view directory for user ");
							}
						}
						
						
						// Fingernail view mode directory check
						photodir = new File(UPLOAD_DIR + USERID
								+ FINGERNAIL_DIR);

						if (!photodir.exists()) {
							boolean created = (new File(UPLOAD_DIR + USERID
									+ FINGERNAIL_DIR)).mkdir();

							if (!created) {
								out.println("Could not create fingernail view directory for user ");
							}
						}
						
						// Determine lowest size of image for fingernail
						File f = new File(TMP_DIR + replacedFileName);
						//BufferedImage sizeImg = ImageIO.read(f);
						//int orgimgHeight = sizeImg.getHeight();
						//int orgimgWidth = sizeImg.getWidth();
						
						imageInfo = new SimpleImageInfo(f);
						int orgimgHeight = imageInfo.getHeight();
						int orgimgWidth = imageInfo.getWidth();
						
						f = null;
						//sizeImg = null;
						
						String fingerNailSize = "";
						
						fingerNailSize = (orgimgWidth < orgimgHeight) ? FINGERNAIL_LOWEST_SIDE + "x" : "x" + FINGERNAIL_LOWEST_SIDE ;
						
						// Now convert that using process
						final Runtime r = Runtime.getRuntime();
						final Process p;
						
						// ImageMagick terminal command (convert)
						
						String command = CONVERT_COMMAND + " " + TMP_DIR
								+ replacedFileName + "  \\( -resize "
								+ MAX_SINGLE_VIEW_WIDTH + "x"
								+ MAX_SINGLE_VIEW_HEIGHT + "> -write "
								+ UPLOAD_DIR + USERID + SINGLE_DIR
								+ file_name + " \\) "
								
								+ " \\( -resize "
								+ MAX_SPREAD_VIEW_WIDTH + "x"
								+ MAX_SPREAD_VIEW_HEIGHT + "> -write "
								+ UPLOAD_DIR + USERID + SPREAD_DIR
								+ file_name + " \\) "
								
								+ "\\( -resize "
								+ MAX_THUMBNAIL_VIEW_WIDTH + "x"
								+ MAX_THUMBNAIL_VIEW_HEIGHT + "> -write "
								+ UPLOAD_DIR + USERID + THUMBNAIL_DIR
								+ file_name + " \\) "
								
								+ " -resize "								
								+ fingerNailSize + " -gravity Center -crop " 
								+ FINGERNAIL_WIDTH + "x" + FINGERNAIL_HEIGHT 
								+ "+0+0 "
								+ UPLOAD_DIR + USERID + FINGERNAIL_DIR
								+ file_name;
								
						log.error("COMMAND: " + command);
						
						p = r.exec(command);
						
						

						p.waitFor();
						
						// Was trying to use JMagick methods, but that did not work, so pure command was used as discussed above
						/*						
						ImageInfo info = new ImageInfo();
						MagickImage originalImage = new MagickImage(new ImageInfo(TMP_DIR + itemName));
						
						Dimension originalSize = originalImage.getDimension();
						
						int originalWidth = (int) originalSize.getWidth();
						int originalHeight = (int) originalSize.getHeight();
						
						int width = 0;
						int height = 0;
						boolean widthGreater = false;
						
						if (originalWidth > originalHeight)
							widthGreater = true;
						
						// Single
						if (widthGreater) {
							width = MAX_SINGLE_VIEW_WIDTH;
							height = getHeightByWidth(originalWidth, originalHeight, width);
						} else {
							height = MAX_SINGLE_VIEW_HEIGHT;
							width = getWidthByHeight(originalWidth, originalHeight, height);
						}
						
						MagickImage scaledImage = originalImage.scaleImage(width, height);						
						
						scaledImage.setFileName(UPLOAD_DIR + USERID + SINGLE_DIR + 
								UNIX_TIMESTAMP + FILENAME_SEPARATOR + itemName);
						scaledImage.writeImage(info);
						
						// Spread		
						if (widthGreater) {
							width = MAX_SPREAD_VIEW_WIDTH;
							height = getHeightByWidth(originalWidth, originalHeight, width);
						} else {
							height = MAX_SPREAD_VIEW_HEIGHT;
							width = getWidthByHeight(originalWidth, originalHeight, height);
						}
						
						scaledImage = originalImage.scaleImage(width, height);
						
						scaledImage.setFileName(UPLOAD_DIR + USERID + SPREAD_DIR + 
								UNIX_TIMESTAMP + FILENAME_SEPARATOR + itemName);
						scaledImage.writeImage(info);
						
						// Thumb
						if (widthGreater) {
							width = MAX_THUMBNAIL_VIEW_WIDTH;
							height = getHeightByWidth(originalWidth, originalHeight, width);
						} else {
							height = MAX_THUMBNAIL_VIEW_HEIGHT;
							width = getWidthByHeight(originalWidth, originalHeight, height);
						}
						
						scaledImage = originalImage.scaleImage(width, height);
									
						scaledImage.setFileName(UPLOAD_DIR + USERID + THUMBNAIL_DIR + 
								UNIX_TIMESTAMP + FILENAME_SEPARATOR + itemName);
						scaledImage.writeImage(info);					

						//out.println(itemName + " uploaded. <br />");
						*/
					} catch (Exception e) {
						log.error(e.getMessage());
					}finally{
						//session.setAttribute("timespend",spendTimes+"#"+numofitems);
					}
				}
			}

			log.error("READY to inter image update in project table");			
			
			// If we have images inserted we need to update them to a project.
			if (projectId!="il" && projectId!="" && StringUtils.isNotEmpty(projectId) && StringUtils.isNotEmpty(imageIds)) {
				log.error("ImageIds: " + imageIds);
				
				imageIds = StringUtils.substringBeforeLast(imageIds, IMAGE_DELIMETER);				
				//imageIds.substring(0, imageIds.length() - 1);				
				
				
				log.error("ImageIds (after length -1 ): " + imageIds);
				
				// Photo upload. We update photo's id
				// Update photos
				Connection conn = new DatabaseInfo().getConnection();
				ResultSet rs = null;
				//String images = "";

				try {
					PreparedStatement stmt1 = conn
							.prepareStatement("SELECT images FROM Project where project_id = ?");

					stmt1.setInt(1, Integer.parseInt(projectId));
					rs = stmt1.executeQuery();

					String updatableImages = "";

					while (rs.next()) {
						updatableImages = rs.getString("images");
					}

					log.error("Updatable Images from database: " + updatableImages);
					//images = images.trim();

					// Why you might get null here?
					// rs.getString can return null
					if (updatableImages != null && updatableImages.length() > 0)
						updatableImages = updatableImages + IMAGE_DELIMETER + imageIds;
					else
						updatableImages = imageIds;
					
					
					log.error("Final updatableImages: " + updatableImages);

					PreparedStatement stmt2 = conn
							.prepareStatement("UPDATE Project set images = ? WHERE project_id = ?");

					stmt2.setString(1, updatableImages);
					stmt2.setInt(2, Integer.parseInt(projectId));
					stmt2.execute();

					log.error("Sucessfully updated image information in project");	
					log.error("projectid to update UserTable::"+projectId);
				
					if(UpdateSelectedProjectId.setSelectedProjectId(conn, Integer.parseInt(projectId), USERID)>0)
						log.error("Sucessfully updated selectedProjectId in UserTable");
					

				} catch (SQLException ex) {
					out.println("Oops!!. Sorry my fault!! Could not add images to project <br/>");
					log.error(ex.getMessage());
					return;
				} finally {
					conn.close();
					if(session.getAttribute("dwnspeed")!= null){
						log.error("Not null session");
						dwnspeedandsize = session.getAttribute("dwnspeed").toString();
					}
					else{
						log.error("null session");
						dwnspeedandsize = "0#0";
					}

					log.error("pass2dd");
					dwnsize = dwnspeedandsize.split("#");
					log.error("down;;"+dwnsize[0]);
					log.error("size;;"+dwnsize[1]);
					sizes = dwnsize[1];
					if(sizes.substring(0,1)=="." || sizes.substring(0,1).equals(".") ){
						sizes = "0"+sizes;
					}
					info  = numofitems+" items totalling "+ sizes +" MB in "+spendTimes+" secs at "+dwnsize[0]+" kb/sec";
				}			
				
			}
		}
	}
	%>
<h1 id="complete">Upload Complete</h1>

<h2><%=info%></h2>
<script>
parent.document.all['target_upload'].onload="parent.document.all['target_upload'].style.display='block';";
</script>	
	
</body>
</html>
