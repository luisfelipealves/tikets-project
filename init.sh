#!/bin/bash

# Cores para o terminal ficar bonito e legível
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${BLUE}==================================================${NC}"
echo -e "${BLUE}   Tickets Project - Inicializador de Ambiente    ${NC}"
echo -e "${BLUE}==================================================${NC}"

# 1. Verificar se o Docker/Kubernetes está a rodar
echo -e "\n${YELLOW}[1/4] Verificando conexão com o Kubernetes...${NC}"
if ! kubectl cluster-info &> /dev/null; then
    echo -e "${RED}❌ Erro: O Docker Desktop ou o Kubernetes não estão rodando!${NC}"
    echo -e "${RED}Por favor, ligue o Docker Desktop e tente novamente.${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Kubernetes está ativo!${NC}"

# 2. Criar Namespace e Instalar o ArgoCD
echo -e "\n${YELLOW}[2/4] Instalando o ArgoCD no cluster...${NC}"
kubectl create namespace argocd --dry-run=client -o yaml | kubectl apply -f -
kubectl apply -n argocd --server-side --force-conflicts -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

# 3. O SEGREDO: Aguardar os CRDs ficarem prontos (Evita o erro que tiveste)
echo -e "\n${YELLOW}[3/4] Aguardando os CRDs do ArgoCD ficarem prontos...${NC}"
echo "Isto pode levar alguns segundos..."
kubectl wait --for=condition=established --timeout=60s crd/applications.argoproj.io
echo -e "${GREEN}✓ CRDs instalados com sucesso!${NC}"

# 4. Aplicar a Root Application do GitOps
echo -e "\n${YELLOW}[4/4] Conectando o cluster ao GitOps (ArgoCD)...${NC}"
if [ -f "infrastructure/k8s/install-argo.yaml" ]; then
    kubectl apply -f infrastructure/k8s/install-argo.yaml
    echo -e "${GREEN}==================================================${NC}"
    echo -e "${GREEN}🎉 SUCESSO! O ArgoCD já está a sincronizar o projeto.${NC}"
    echo -e "${GREEN}O seu ambiente estará pronto em instantes.${NC}"
    echo -e "${GREEN}==================================================${NC}"
else
    echo -e "${RED}❌ Erro: Ficheiro infrastructure/k8s/install-argo.yaml não encontrado!${NC}"
    exit 1
fi