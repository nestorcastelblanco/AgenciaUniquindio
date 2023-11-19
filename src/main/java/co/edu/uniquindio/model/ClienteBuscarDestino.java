package co.edu.uniquindio.model;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ClienteBuscarDestino implements Serializable {
   private Clientes cliente;
   private String destino;
}