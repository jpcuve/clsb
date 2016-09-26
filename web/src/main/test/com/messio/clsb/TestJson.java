package com.messio.clsb;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.messio.clsb.entity.Instruction;
import org.junit.Test;

import java.io.IOException;
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

/*
        final Instruction instruction1 = objectMapper.readValue("{\"@class\":\".Instruction\",\"when\":\"15:07:10.892\",\"account\":\"test account\"}", Instruction.class);
        System.out.println(instruction1);
*/

    }
}
