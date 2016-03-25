
# If directory with same name exists delete it 
# if DELETE_PROJECT_WITH_SAME_NAME set to y
if [ $DELETE_PROJECT_WITH_SAME_NAME = "y" ]; then
	rm -rf $ARTIFACT_ID
	echo """Deleted previous project with name """$ARTIFACT_ID""". Configure \"DELETE_PROJECT_WITH_SAME_NAME\" if you want otherwise."""	
	echo ""
fi

# Do what ever is written in database script file
if [ $DATA_BASE_SCRIPT = "y" ]; then
	source "$DATA_DIRECTORY/$DATABASE_SCRIPT_FILENAME"
	echo "Successfully created database $DATABASE_NAME"
	echo ""
fi

# Lets create appropriate directory using pom.xml
# from DATA_DIRECTORY

echo ""
if [ ! -d $ARTIFACT_ID ]; then
	mkdir $ARTIFACT_ID
	cd $ARTIFACT_ID

	mkdir -p $PROJECT_DIRECTORY_STRUCTURE
	source $DATA_DIRECTORY/pom.xml.sh > pom.xml
	source $DATA_DIRECTORY/web.xml.sh > src/main/webapp/WEB-INF/web.xml
	cp $DATA_DIRECTORY/messages.properties src/main/webapp/WEB-INF/messages/
	source $DATA_DIRECTORY/spring.properties.sh > src/main/webapp/WEB-INF/cfg/spring.properties
	source $DATA_DIRECTORY/hibernate.cfg.xml.sh > src/main/webapp/WEB-INF/hibernate.cfg.xml
	source $DATA_DIRECTORY/spring-servlet.xml.sh > src/main/webapp/WEB-INF/$SERVLET_NAME-servlet.xml

	if [ $USE_DATA_CONF = "y" ]; then
		source $DATA_DIRECTORY/spring-data.xml.sh > src/main/webapp/WEB-INF/$DATA_CONF
	fi

	if [ $CREATE_WELCOME_CONTROLLER = "y" ]; then
		source "$DATA_DIRECTORY/welcomeclass.sh"
	fi

	if [ $CREATE_GENERIC_DAO = "y" ]; then
		source "$DATA_DIRECTORY/genericdao.sh"
	fi

	if [ $CREATE_GENERIC_SERVICE = "y" ]; then
		source "$DATA_DIRECTORY/genericservice.sh"
	fi

	if [ $USE_REFERENCE_IMPLEMENTATION="y" ]; then
		source "$DATA_DIRECTORY/referenceimplementation.sh"
	fi

	echo "Project \"$ARTIFACT_ID\" successfully created. !!"	
	echo ""
else
	echo "Sorry project directory already exists. !!"
	echo ""
fi
