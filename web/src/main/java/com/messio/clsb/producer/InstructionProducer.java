package com.messio.clsb.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.messio.clsb.entity.Instruction;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by jpc on 27-05-17.
 */
public class InstructionProducer {
    public static final Logger LOGGER = Logger.getLogger(InstructionProducer.class.getCanonicalName());

    @Produces
    @ApplicationScoped
    public List<Instruction> createInstructions(){
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        try (final InputStream is = getClass().getClassLoader().getResourceAsStream("com/messio/clsb/scenario-01.json")){
            return Arrays.asList(objectMapper.readValue(is, Instruction[].class));
        } catch(IOException e){
            LOGGER.severe("cannot read instructions, " + e.getMessage());
        }
        return Collections.emptyList();
    }
}
