package br.com.simplehealth.estoque.util;

import br.com.simplehealth.estoque.model.Alimento;
import br.com.simplehealth.estoque.model.Hospitalar;
import br.com.simplehealth.estoque.model.Item;
import br.com.simplehealth.estoque.model.Medicamento;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Deserializador customizado para Item que decide qual subclasse instanciar
 * baseado nos campos presentes no JSON
 */
public class ItemDeserializer extends JsonDeserializer<Item> {
    
    private static final Logger logger = LoggerFactory.getLogger(ItemDeserializer.class);
    
    @Override
    public Item deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p);
        
        // Verifica quais campos específicos estão presentes para determinar o tipo
        boolean hasPrescricao = node.has("prescricao") && !node.get("prescricao").isNull();
        boolean hasTarga = node.has("targa") && !node.get("targa").isNull();
        boolean hasAlergenicos = node.has("alergenicos") && !node.get("alergenicos").isNull();
        boolean hasDescartabilidade = node.has("descartabilidade") && !node.get("descartabilidade").isNull();
        
        try {
            // Medicamento tem prescricao ou targa
            if (hasPrescricao || hasTarga) {
                return mapper.treeToValue(node, Medicamento.class);
            }
            // Alimento tem alergenicos
            else if (hasAlergenicos) {
                return mapper.treeToValue(node, Alimento.class);
            }
            // Hospitalar tem descartabilidade
            else if (hasDescartabilidade) {
                return mapper.treeToValue(node, Hospitalar.class);
            }
            // Default: Medicamento
            else {
                logger.warn("Não foi possível determinar o tipo do item, usando Medicamento como padrão");
                return mapper.treeToValue(node, Medicamento.class);
            }
        } catch (Exception e) {
            logger.error("Erro ao deserializar Item: {}", e.getMessage());
            throw new IOException("Erro ao deserializar Item", e);
        }
    }
}
