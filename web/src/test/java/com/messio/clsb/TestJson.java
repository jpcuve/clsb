package com.messio.clsb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.messio.clsb.entity.Instruction;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;

/**
 * Created by jpc on 9/26/16.
 */
public class TestJson {
    @Test
    public void testPolymorphic() throws IOException {
        final Instruction instruction = new Instruction();
        instruction.setWhen(LocalTime.now());
        instruction.setAccount("test account");
        final ObjectMapper objectMapper = new ObjectMapper();
        final JaxbAnnotationModule annotationModule = new JaxbAnnotationModule();
        objectMapper.registerModule(annotationModule);
        objectMapper.writeValue(System.out, instruction);
    }

    @Test
    public void testPolymorphicReadValue() throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final JaxbAnnotationModule annotationModule = new JaxbAnnotationModule();
        objectMapper.registerModule(annotationModule);
        final InputStream is = getClass().getClassLoader().getResourceAsStream("scenario-test.json");
        final Instruction[] instructions = objectMapper.readValue(is, Instruction[].class);
        for (final Instruction instruction: instructions){
            System.out.println(instruction);
        }
    }
}
