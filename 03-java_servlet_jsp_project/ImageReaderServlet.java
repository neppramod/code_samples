package ..;

import static ...util.Paths.SINGLE_DIR;
import static ...util.Paths.SPREAD_DIR;
import static ...util.Paths.THUMBNAIL_DIR;
import static ...util.Paths.FINGERNAIL_DIR;
import static ...util.Paths.UPLOAD_DIR;
import static ...util.Utils.SINGLE_VIEW;
import static ...util.Utils.SPREAD_VIEW;
import static ...util.Utils.FINGERNAIL_VIEW;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import ...util.CookieManager;
import ...util.DatabaseInfo;

public class ImageReaderServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		OutputStream streamOut = response.getOutputStream();

		// Cookie check
		CookieManager cookieManager = new CookieManager();

		cookieManager.checkCookie(request, response);

		if (cookieManager.getUserValue() == null)
			return;

		String imageIdStr = request.getParameter("id");
		String type = request.getParameter("type");
		Integer userId = null;
		Integer imageId = null;
		String imageName = null;
		final Logger log = Logger.getLogger("ImageReaderServlet");

		if (StringUtils.isNumeric(imageIdStr)
				&& StringUtils.isNotEmpty(imageIdStr)
				&& StringUtils.isNotEmpty(type)) {
			imageId = Integer.parseInt(imageIdStr);
			String imagePath = null;

			if (type.equals(SINGLE_VIEW))
				imagePath = SINGLE_DIR;
			else if (type.equals(SPREAD_VIEW))
				imagePath = SPREAD_DIR;
			else if (type.equals(FINGERNAIL_VIEW))
				imagePath = FINGERNAIL_DIR;
			else
				imagePath = THUMBNAIL_DIR;

			Connection conn = null;
			ResultSet rs = null;
			PreparedStatement stmt1 = null;
			try {
				conn = new DatabaseInfo().getConnection();
				stmt1 = conn
						.prepareStatement("SELECT file_name AS name, user_table_id AS userid FROM Image where image_id = ?");

				stmt1.setInt(1, imageId);
				rs = stmt1.executeQuery();

				while (rs.next()) {
					userId = rs.getInt("userid");
					imageName = rs.getString("name");
				}

			} catch (SQLException ex) {
				ex.printStackTrace();
			} finally {
				if (conn != null) {
					try {
						if (rs != null)
							rs.close();

						if (stmt1 != null)
							stmt1.close();

						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			String fileUrl = UPLOAD_DIR + userId + imagePath + imageName;
			File image = new File(fileUrl);
			response.setContentLength((int) image.length());
			response.setContentType("image/"
					+ imageName.substring(imageName.lastIndexOf(".") + 1));
			InputStream streamIn = new FileInputStream(image);
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = streamIn.read(buffer)) >= 0) {
				streamOut.write(buffer, 0, count);
			}
			streamOut.flush();
			streamOut.close();
			return;
		}
	}
}

