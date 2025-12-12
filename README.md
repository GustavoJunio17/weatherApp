# WeatherApp 🌦️

Aplicação Android nativa desenvolvida para consulta de previsão do tempo.

## 👨‍🎓 Identificação do Aluno

* **Nome:** Gustavo Junio Ferreira Rodrigues
* **Curso:** Sistemas de Informação
* **Período:** 6° Período
* **Disciplina:** Programação III

---

## 📱 Descrição da Aplicação

O **WeatherApp** é um aplicativo Android desenvolvido em **Java** que permite ao usuário consultar as condições climáticas atuais de uma determinada cidade.

**Funcionalidades principais:**
* Busca de cidades por nome.
* Exibição da temperatura, umidade e descrição do clima.
* Interface intuitiva.
* Consumo de API REST em background para obtenção dos dados (JSON).

---

## 🛠️ Instruções para Execução

Para rodar o projeto localmente, siga os passos abaixo:

### Pré-requisitos
* Android Studio instalado (versão recomendada: Ladybug ou mais recente).
* JDK 11 ou superior.
* Dispositivo Android ou Emulador configurado.

### Passo a passo

1.  **Clonar o repositório:**
    ```bash
    git clone [https://github.com/GustavoJunio17/weatherApp.git](https://github.com/GustavoJunio17/weatherApp.git)
    ```

2.  **Abrir no Android Studio:**
    * Abra o Android Studio e selecione "Open".
    * Navegue até a pasta onde o projeto foi clonado.

3.  **Compilar e Executar:**
    * Aguarde o Gradle finalizar o download das dependências.
    * Clique no botão **Run** (ícone de play verde) e selecione seu emulador ou dispositivo físico.

---

---

## 🌐 Exemplo da URL utilizada na Requisição

A aplicação consome a API obrigatória hospedada no Elastic Beanstalk (AWS). Abaixo está o formato da URL utilizada:

**Endpoint Base:**
http://agent-weathermap-env-env.eba-6pzgqekp.us-east-2.elasticbeanstalk.com/api/weather


**Exemplo completo da chamada (GET):**
http://agent-weathermap-env-env.eba-6pzgqekp.us-east-2.elasticbeanstalk.com/api/weather?city=Passos, MG, BR&days=7&APPID=AgentWeather2024_a8f3b9c1d7e2f5g6h4i9j0k112m3n4o5p6

**Parâmetros:**
* `city`: Nome da cidade (Ex: `Passos, MG, BR`).
* `days`: Quantidade de dias da previsão (Ex: `7`).
* `APPID`: Chave fixa da API (`AgentWeather2024_a8f3b9c1d7e2f5g6h4i9j0k112m3n4o5p6`).

*Nota: A API retorna os dados sempre em Celsius e utiliza ícones embutidos no JSON (emojis), sem necessidade de parâmetro `units`.*
