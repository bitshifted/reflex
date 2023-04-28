/*
 *
 *  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
 *  *
 *  * This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 */

package co.bitshifted.reflex.core.serialize.xml;

import co.bitshifted.reflex.core.exception.BodySerializationException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JaxbXmlBodySerializer implements XmlBodySerializer {

  private static final Logger LOGGER = LoggerFactory.getLogger(JaxbXmlBodySerializer.class);

  private final Consumer<Marshaller> marshallerCustomizer;
  private final Consumer<Unmarshaller> unmarshallerCustomizer;

  public JaxbXmlBodySerializer() {
    this.marshallerCustomizer = marshaller -> {};
    this.unmarshallerCustomizer = unmarshaller -> {};
  }

  public JaxbXmlBodySerializer(
      Consumer<Marshaller> marshallerCustomizer, Consumer<Unmarshaller> unmarshallerCustomizer) {
    this.marshallerCustomizer = marshallerCustomizer;
    this.unmarshallerCustomizer = unmarshallerCustomizer;
  }

  @Override
  public <T> InputStream objectToStream(T object) {
    try {
      var ctx = JAXBContext.newInstance(object.getClass());
      var marshaller = ctx.createMarshaller();
      marshallerCustomizer.accept(marshaller);
      var os = new ByteArrayOutputStream();
      marshaller.marshal(object, os);
      os.close();
      return new ByteArrayInputStream(os.toByteArray());
    } catch (JAXBException | IOException ex) {
      throw new BodySerializationException("Failed to write object", ex);
    }
  }

  @Override
  public <T> T streamToObject(InputStream input, Class<T> type) {
    try {
      var ctx = JAXBContext.newInstance(type);
      var unmarshaller = ctx.createUnmarshaller();
      unmarshallerCustomizer.accept(unmarshaller);
      return (T) unmarshaller.unmarshal(input);
    } catch (JAXBException ex) {
      throw new BodySerializationException("Failed to write object", ex);
    }
  }
}
