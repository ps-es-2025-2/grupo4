\@startuml

left to right direction

actor \"Técnico/Farmacêutico\" as A1

actor \"Gestor Administrativo\" as A2

actor \"Sistema\" as S

rectangle \"Rastreabilidade de Controlados\" {

usecase \"Registrar Entrada de Lote Controlado\" as UC1

usecase \"Registrar Saída (Consumo/Dispensação)\" as UC2

usecase \"Rastrear Caminho do Item (Auditoria)\" as UC3

usecase \"Emitir Alerta de Validade\" as UC4

A1 \--\> UC1 : Registra Lote

A1 \--\> UC2 : Registra Consumo/Dispensa

A2 \--\> UC3 : Solicita Rastreio

UC2 .\> UC1 : \<\<include\>\> \\n(Depende da Entrada)

UC2 .\> UC3 : \<\<include\>\> \\n(Gera o dado de saída)

UC1 ..\> UC4 : \<\<include\>\> \\n(Dados de entrada alimentam o alerta)

S \--\> UC4 : Emite Alerta

}

\@enduml

![image1.png](images/image1.png)
