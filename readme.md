# TransactionExtractor

**AI-Powered Financial Transaction Extraction from PDFs using RAG**

## Overview
TransactionExtractor is a Spring Boot application that intelligently extracts and analyzes financial transactions from PDF documents using LangChain4j and OpenAI's GPT models. It employs Retrieval-Augmented Generation (RAG) to provide accurate, context-aware transaction analysis with structured JSON responses.

## Key Features
- **PDF Upload & Processing** - Upload PDF documents directly via REST API for automatic ingestion
- **Vector Embeddings** - Converts document content into semantic embeddings stored in PostgreSQL with pgvector
- **RAG-Powered Analysis** - Retrieves relevant transaction context before AI processing for improved accuracy
- **Structured JSON Output** - Returns transactions in a well-defined JSON format with fields: `from`, `to`, `amount`, `time`
- **Time-Based Filtering** - Query transactions from specific date ranges (e.g., last 3 days)
- **Secure Configuration** - Environment-specific profiles to protect sensitive API keys and credentials

## Tech Stack
- **Framework**: Spring Boot 3.x
- **AI/ML**: LangChain4j, OpenAI API (GPT-4.5-mini, Text Embeddings)
- **Database**: PostgreSQL with pgvector extension
- **PDF Processing**: Apache PDFBox
- **Architecture**: REST API with RAG pipeline

## How It Works
1. Upload a PDF containing transaction records
2. Document is parsed and converted to semantic embeddings
3. Embeddings are stored in PostgreSQL vector database
4. Query transactions with natural language (e.g., "Get recent transactions")
5. System retrieves relevant context using semantic search
6. OpenAI analyzes context + query and returns structured JSON

## API Endpoints

### Upload PDF
```bash
POST /api/transactions/upload
Content-Type: multipart/form-data

curl -X POST http://localhost:8081/api/transactions/upload \
  -F "file=@transactions.pdf"

Response:
{
  "message": "PDF content successfully indexed into Vector Store."
}
```

### Get Recent Transactions
```bash
GET /api/transactions/recent

Response:
{
  "transactions": [
    {
      "from": "John Doe",
      "to": "Jane Smith",
      "amount": 500.00,
      "time": "2026-06-28T10:30:00"
    }
  ],
  "message": "Found 5 transactions in the last 3 days"
}
```

## Getting Started

### Prerequisites
- Java 17+
- PostgreSQL with pgvector extension
- OpenAI API key
- Gradle or Maven

### Installation
1. Clone the repository
```bash
git clone <repository-url>
cd TransactionExtractor/TrasactionExtractor
```

2. Setup local configuration
```bash
cp src/main/resources/application-local.yaml.example src/main/resources/application-local.yaml
```

3. Add credentials to `application-local.yaml`
```yaml
spring:
  datasource:
    username: your_db_username
    password: your_db_password

openai:
  api-key: your-openai-api-key
```

4. Set Spring profile
```bash
export SPRING_PROFILES_ACTIVE=local
```

5. Run the application
```bash
./gradlew bootRun
# or
mvn spring-boot:run
```

The server will start at `http://localhost:8081`

### Configuration
- **Database**: PostgreSQL running on `localhost:5432/textr`
- **Server Port**: `8081`
- **OpenAI Models**: 
  - Chat: `gpt-4.5-mini`
  - Embeddings: `text-embedding-3-large`

## Security
- Sensitive configurations stored in profile-specific YAML files (gitignored)
- API keys and database credentials excluded from version control
- Environment-based configuration for different deployment stages
- Use `application-local.yaml` for development (gitignored)
- Use `application-prod.yaml` for production (gitignored)

### Managing Secrets
Never commit the following files:
- `application-local.yaml` (local secrets)
- `application-prod.yaml` (production secrets)

Always use `.example` files as templates and populate with your actual credentials.

## Project Structure
```
src/
├── main/
│   ├── java/com/sourav/trasactionextractor/
│   │   ├── controller/      # REST API endpoints
│   │   ├── service/         # Business logic & RAG pipeline
│   │   ├── intf/            # LangChain4j AI service interface
│   │   └── model/           # Data models (TransactionResponse, etc)
│   └── resources/
│       ├── application.yaml                 # Common config (committed)
│       ├── application-local.yaml           # Local secrets (gitignored)
│       ├── application-local.yaml.example   # Template (committed)
│       └── application-prod.yaml            # Prod secrets (gitignored)
```

## Future Enhancements
- Multi-format document support (Excel, CSV)
- Custom transaction categorization
- Batch processing for multiple PDFs
- Transaction reconciliation and anomaly detection
- Advanced filtering and search capabilities
- Audit logging and transaction history

## Contributing
1. Create a feature branch
2. Make your changes
3. Ensure no secrets are committed
4. Submit a pull request

## Support
For issues or questions, please open an issue on the repository.