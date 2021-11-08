package com.mykhailopavliuk.repository.impl;

import com.mykhailopavliuk.exception.SettingsException;
import com.mykhailopavliuk.model.Settings;
import com.mykhailopavliuk.repository.SettingsRepository;
import com.mykhailopavliuk.util.SettingsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Repository
public class SettingsRepositoryImpl implements SettingsRepository {

    private final Path settingsPath;
    private final Path defaultSettingsPath;
    private final Path settingsSchemaPath;

    @Autowired
    public SettingsRepositoryImpl(@Qualifier("settings") Path settingsPath,
                                  @Qualifier("defaultSettings") Path defaultSettingsPath,
                                  @Qualifier("settingsSchema") Path settingsSchemaPath) {
        this.settingsPath = settingsPath;
        this.defaultSettingsPath = defaultSettingsPath;
        this.settingsSchemaPath = settingsSchemaPath;
    }

    @Override
    public Settings save(Settings settings) {
        JAXBContext contextObj;
        try {
            contextObj = JAXBContext.newInstance(Settings.class);
            Marshaller marshallerObj = contextObj.createMarshaller();
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshallerObj.marshal(settings, settingsPath.toFile());
            return settings;
        } catch (JAXBException ex) {
            throw new SettingsException("Exception has occurred while saving " + settingsPath.getFileName() + " file");
        }

    }

    @Override
    public Settings find() {
        try {
            checkIfSettingsFileValid();
        } catch (SettingsException e) {
            createDefault();
        }

        return parseSetting(settingsPath);

    }

    @Override
    public void delete() {
        try {
            Files.deleteIfExists(settingsPath);
        } catch (IOException e) {
            throw new SettingsException("Exception has occurred while deleting " + settingsPath.getFileName() + " file");
        }
    }

    private void checkIfSettingsFileValid() {
        try {
            Reader reader = Files.newBufferedReader(settingsPath);
            String schemaLang = XMLConstants.W3C_XML_SCHEMA_NS_URI;
            SchemaFactory factory = SchemaFactory.newInstance(schemaLang);
            Schema schema = factory.newSchema(settingsSchemaPath.toFile());
            Validator validator = schema.newValidator();
            SAXSource source = new SAXSource(new InputSource(reader));
            validator.validate(source);

        } catch (SAXException | IOException e) {
            throw new SettingsException("Exception has occurred while validating " + settingsPath.getFileName() + " file");
        }
    }

    private void createDefault() {
        try {
            if (!settingsPath.toFile().exists()) Files.createFile(settingsPath);
            Files.copy(defaultSettingsPath, settingsPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new SettingsException("Exception has occurred while creating default " + settingsPath.getFileName() + " file");
        }
    }

    private static SAXParser createSaxParser() throws ParserConfigurationException, SAXException {
        SAXParser saxParser;
        SAXParserFactory factory = SAXParserFactory.newInstance();
        saxParser = factory.newSAXParser();
        return saxParser;
    }

    private Settings parseSetting(Path fileXML) {
        SettingsHandler handler = new SettingsHandler();
        try {
            SAXParser parser = createSaxParser();
            parser.parse(fileXML.toFile(), handler);
            return handler.getSettings();
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            throw new SettingsException("Exception has occurred while parsing " + settingsPath.getFileName() + " file");
        }

    }
}




