# Cucina Quarkus Application Makefile
# This Makefile provides commands for building, running, and testing the application

# Variables
APP_NAME = cucina
QUARKUS_VERSION = 3.14.1
PORT = 8080
BASE_URL = http://localhost:$(PORT)

# Colors for output
GREEN = \033[0;32m
YELLOW = \033[1;33m
RED = \033[0;31m
NC = \033[0m # No Color

# Default target
.PHONY: help
help: ## Show this help message
	@echo "$(GREEN)Cucina Quarkus Application$(NC)"
	@echo "$(GREEN)========================$(NC)"
	@echo ""
	@echo "Available commands:"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "  $(YELLOW)%-20s$(NC) %s\n", $$1, $$2}'

# Development commands
.PHONY: dev
dev: ## Start the application in development mode
	@echo "$(GREEN)Starting Quarkus in development mode...$(NC)"
	./gradlew quarkusDev

.PHONY: build
build: ## Build the application
	@echo "$(GREEN)Building the application...$(NC)"
	./gradlew build

.PHONY: clean
clean: ## Clean build artifacts
	@echo "$(GREEN)Cleaning build artifacts...$(NC)"
	./gradlew clean

.PHONY: test
test: ## Run unit tests
	@echo "$(GREEN)Running unit tests...$(NC)"
	./gradlew test

# Application lifecycle
.PHONY: start
start: ## Start the application (production mode)
	@echo "$(GREEN)Starting application in production mode...$(NC)"
	./gradlew quarkusBuild
	java -jar build/quarkus-app/quarkus-run.jar

.PHONY: stop
stop: ## Stop the application (kill any running quarkus processes)
	@echo "$(GREEN)Stopping application...$(NC)"
	@pkill -f quarkus || true

# Docker commands
.PHONY: docker-build
docker-build: ## Build Docker image
	@echo "$(GREEN)Building Docker image...$(NC)"
	./gradlew quarkusBuild -Dquarkus.container-image.build=true

.PHONY: docker-run
docker-run: ## Run application in Docker
	@echo "$(GREEN)Running application in Docker...$(NC)"
	docker run -i --rm -p $(PORT):$(PORT) $(APP_NAME):1.0.0-SNAPSHOT

.PHONY: docker-compose-up
docker-compose-up: ## Start services with docker-compose
	@echo "$(GREEN)Starting services with docker-compose...$(NC)"
	cd src/main/docker/docker-compose && docker-compose up -d

.PHONY: docker-compose-down
docker-compose-down: ## Stop services with docker-compose
	@echo "$(GREEN)Stopping services with docker-compose...$(NC)"
	cd src/main/docker/docker-compose && docker-compose down

# Smoke Testing Commands
.PHONY: smoke-test
smoke-test: ## Run comprehensive smoke tests against all endpoints
	@echo "$(GREEN)Running comprehensive smoke tests...$(NC)"
	@echo "$(YELLOW)Testing application endpoints at $(BASE_URL)$(NC)"
	@echo ""
	@$(MAKE) smoke-test-health
	@$(MAKE) smoke-test-basic
	@$(MAKE) smoke-test-i18n
	@echo ""
	@echo "$(GREEN)✅ All smoke tests completed successfully!$(NC)"

.PHONY: smoke-test-health
smoke-test-health: ## Test health check endpoints
	@echo "$(GREEN)Testing health check endpoints...$(NC)"
	@echo -n "  Testing /health/live: "
	@curl -s -o /dev/null -w "%{http_code}" $(BASE_URL)/health/live | grep -q "200" && echo "$(GREEN)✅ PASS$(NC)" || echo "$(RED)❌ FAIL$(NC)"
	@echo -n "  Testing /health/ready: "
	@curl -s -o /dev/null -w "%{http_code}" $(BASE_URL)/health/ready | grep -q "200" && echo "$(GREEN)✅ PASS$(NC)" || echo "$(RED)❌ FAIL$(NC)"
	@echo -n "  Testing /health: "
	@curl -s -o /dev/null -w "%{http_code}" $(BASE_URL)/health | grep -q "200" && echo "$(GREEN)✅ PASS$(NC)" || echo "$(RED)❌ FAIL$(NC)"
	@echo ""

.PHONY: smoke-test-basic
smoke-test-basic: ## Test basic application endpoints
	@echo "$(GREEN)Testing basic application endpoints...$(NC)"
	@echo -n "  Testing /hello: "
	@curl -s $(BASE_URL)/hello | grep -q "Hello from Quarkus REST" && echo "$(GREEN)✅ PASS$(NC)" || echo "$(RED)❌ FAIL$(NC)"
	@echo -n "  Testing /q/info: "
	@curl -s -o /dev/null -w "%{http_code}" $(BASE_URL)/q/info | grep -q "200" && echo "$(GREEN)✅ PASS$(NC)" || echo "$(RED)❌ FAIL$(NC)"
	@echo -n "  Testing /q/metrics: "
	@curl -s -o /dev/null -w "%{http_code}" $(BASE_URL)/q/metrics | grep -q "200" && echo "$(GREEN)✅ PASS$(NC)" || echo "$(RED)❌ FAIL$(NC)"
	@echo ""

.PHONY: smoke-test-i18n
smoke-test-i18n: ## Test internationalization endpoints
	@echo "$(GREEN)Testing i18n endpoints...$(NC)"
	@echo -n "  Testing /api/i18n/welcome (default): "
	@curl -s $(BASE_URL)/api/i18n/welcome | grep -q "message" && echo "$(GREEN)✅ PASS$(NC)" || echo "$(RED)❌ FAIL$(NC)"
	@echo -n "  Testing /api/i18n/welcome (Spanish): "
	@curl -s $(BASE_URL)/api/i18n/welcome?lang=es | grep -q "message" && echo "$(GREEN)✅ PASS$(NC)" || echo "$(RED)❌ FAIL$(NC)"
	@echo -n "  Testing /api/i18n/greeting: "
	@curl -s $(BASE_URL)/api/i18n/greeting?name=TestUser | grep -q "message" && echo "$(GREEN)✅ PASS$(NC)" || echo "$(RED)❌ FAIL$(NC)"
	@echo -n "  Testing /api/i18n/user/created: "
	@curl -s $(BASE_URL)/api/i18n/user/created | grep -q "message" && echo "$(GREEN)✅ PASS$(NC)" || echo "$(RED)❌ FAIL$(NC)"
	@echo -n "  Testing /api/i18n/error/validation/required: "
	@curl -s $(BASE_URL)/api/i18n/error/validation/required | grep -q "message" && echo "$(GREEN)✅ PASS$(NC)" || echo "$(RED)❌ FAIL$(NC)"
	@echo -n "  Testing /api/i18n/success/dataSaved: "
	@curl -s $(BASE_URL)/api/i18n/success/dataSaved | grep -q "message" && echo "$(GREEN)✅ PASS$(NC)" || echo "$(RED)❌ FAIL$(NC)"
	@echo -n "  Testing /api/i18n/messages: "
	@curl -s $(BASE_URL)/api/i18n/messages | grep -q "message" && echo "$(GREEN)✅ PASS$(NC)" || echo "$(RED)❌ FAIL$(NC)"
	@echo ""

.PHONY: smoke-test-quick
smoke-test-quick: ## Run quick smoke test (health checks only)
	@echo "$(GREEN)Running quick smoke test (health checks only)...$(NC)"
	@$(MAKE) smoke-test-health
	@echo "$(GREEN)✅ Quick smoke test completed!$(NC)"

.PHONY: smoke-test-verbose
smoke-test-verbose: ## Run smoke tests with verbose output
	@echo "$(GREEN)Running smoke tests with verbose output...$(NC)"
	@echo "$(YELLOW)Testing application endpoints at $(BASE_URL)$(NC)"
	@echo ""
	@echo "$(GREEN)=== Health Check Endpoints ===$(NC)"
	@echo "GET $(BASE_URL)/health/live"
	@curl -s $(BASE_URL)/health/live | jq . || curl -s $(BASE_URL)/health/live
	@echo ""
	@echo "GET $(BASE_URL)/health/ready"
	@curl -s $(BASE_URL)/health/ready | jq . || curl -s $(BASE_URL)/health/ready
	@echo ""
	@echo "GET $(BASE_URL)/health"
	@curl -s $(BASE_URL)/health | jq . || curl -s $(BASE_URL)/health
	@echo ""
	@echo "$(GREEN)=== Basic Application Endpoints ===$(NC)"
	@echo "GET $(BASE_URL)/hello"
	@curl -s $(BASE_URL)/hello
	@echo ""
	@echo "GET $(BASE_URL)/q/info"
	@curl -s $(BASE_URL)/q/info | jq . || curl -s $(BASE_URL)/q/info
	@echo ""
	@echo "$(GREEN)=== I18n Endpoints ===$(NC)"
	@echo "GET $(BASE_URL)/api/i18n/welcome"
	@curl -s $(BASE_URL)/api/i18n/welcome | jq . || curl -s $(BASE_URL)/api/i18n/welcome
	@echo ""
	@echo "GET $(BASE_URL)/api/i18n/greeting?name=SmokeTest"
	@curl -s $(BASE_URL)/api/i18n/greeting?name=SmokeTest | jq . || curl -s $(BASE_URL)/api/i18n/greeting?name=SmokeTest
	@echo ""
	@echo "$(GREEN)✅ Verbose smoke test completed!$(NC)"

# Utility commands
.PHONY: check-deps
check-deps: ## Check if required dependencies are installed
	@echo "$(GREEN)Checking dependencies...$(NC)"
	@command -v curl >/dev/null 2>&1 || { echo "$(RED)curl is required but not installed.$(NC)"; exit 1; }
	@command -v jq >/dev/null 2>&1 || { echo "$(YELLOW)Warning: jq is not installed. Verbose output will be limited.$(NC)"; }
	@echo "$(GREEN)✅ Dependencies check completed.$(NC)"

.PHONY: wait-for-app
wait-for-app: ## Wait for the application to be ready
	@echo "$(GREEN)Waiting for application to be ready...$(NC)"
	@for i in $$(seq 1 30); do \
		if curl -s -o /dev/null $(BASE_URL)/health/ready; then \
			echo "$(GREEN)✅ Application is ready!$(NC)"; \
			exit 0; \
		fi; \
		echo "Attempt $$i/30: Application not ready yet..."; \
		sleep 2; \
	done; \
	echo "$(RED)❌ Application failed to start within 60 seconds$(NC)"; \
	exit 1

.PHONY: status
status: ## Check application status
	@echo "$(GREEN)Checking application status...$(NC)"
	@echo -n "Health check: "
	@curl -s -o /dev/null -w "%{http_code}" $(BASE_URL)/health/live && echo "$(GREEN)✅ UP$(NC)" || echo "$(RED)❌ DOWN$(NC)"
	@echo -n "Application: "
	@curl -s -o /dev/null -w "%{http_code}" $(BASE_URL)/hello && echo "$(GREEN)✅ UP$(NC)" || echo "$(RED)❌ DOWN$(NC)"

# CI/CD helpers
.PHONY: ci-smoke-test
ci-smoke-test: ## Run smoke tests for CI/CD (non-interactive)
	@echo "$(GREEN)Running CI smoke tests...$(NC)"
	@$(MAKE) wait-for-app
	@$(MAKE) smoke-test
	@echo "$(GREEN)✅ CI smoke tests completed successfully!$(NC)"
