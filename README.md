
<div align="center">

# 🧬 ACPDF  
### Advanced Code Plagiarism Detection Framework  

### ⚡ Hybrid Structural + Semantic Detection Engine  
### Built with Java 21 • Spring Boot 3.3.4

<br>

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.3.4-brightgreen?style=for-the-badge&logo=springboot)
![AI](https://img.shields.io/badge/Detection-Hybrid%20Engine-blueviolet?style=for-the-badge)
![Research](https://img.shields.io/badge/Approach-Research%20Driven-black?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Experimentally%20Validated-success?style=for-the-badge)

<br><br>

> **A research-grade plagiarism detection engine that reconstructs code logic to detect similarity—even after aggressive obfuscation.**

</div>

---

## 🧠 Research Context

This project is developed as a **research-oriented system** in the domain of:

- Program Analysis  
- Code Clone Detection  
- Semantic Code Understanding  

It is intended as a foundation for exploring **semantic similarity, AI-assisted reasoning, and large-scale code analysis systems**.

---

# 🚀 Executive Overview

**ACPDF (Advanced Code Plagiarism Detection Framework)** is a high-performance backend system designed to detect **code plagiarism beyond surface-level matching**.

Traditional systems fail when:

- Variables are renamed  
- Methods are reordered  
- Code is reformatted  

ACPDF overcomes this by analyzing **structure, sequence, and statistical patterns**, achieving **near-perfect robustness against obfuscation**.

---

# 🎯 Problem Statement

Most plagiarism detection tools rely on:

- Text similarity  
- Token matching  
- Basic heuristics  

These approaches fail under:

❌ Variable renaming  
❌ Structural refactoring  
❌ Code reordering  
❌ Partial copying  

Resulting in **false negatives and unreliable detection**.

---

# 💡 Solution: Hybrid Detection Engine

ACPDF introduces a **multi-layered detection system**:

## 🧬 Structural Intelligence (AST)
- Captures code structure using Abstract Syntax Trees  
- Immune to identifier renaming  

## 🔗 Sequence Intelligence (LCS)
- Detects logical flow similarity  
- Identifies reordered but equivalent code  

## 📊 Statistical Intelligence (TF-IDF)
- Models coding style patterns  
- Detects stylistic similarities  

## ⚖️ Hybrid Model

```

Final Score =
0.40 × AST +
0.30 × LCS +
0.30 × TF-IDF

```

---

# 🔬 Experimental Validation

## 🧪 Robustness Experiment

- Dataset: 30+ Java files  
- Attack: Variable Renaming Mutation  
- Tool: Custom MutatorEngine  

### 📈 Results

- Mean Robustness: **~99.85%**  
- AST Accuracy: **100%**  

---

## 📊 Statistical Significance

- Test: McNemar’s Test  
- Chi-Square: **27.0345**  
- Confidence: **p < 0.05**  

### ✅ Conclusion

Hybrid model improvement is **statistically significant**.

---

# ⚠️ Research Gap & Limitations

While ACPDF achieves high robustness against syntactic obfuscation, several key limitations remain:

- The system primarily captures **structural similarity**, not full semantic equivalence  
- It struggles with **semantically equivalent but structurally different programs**  
- Current approach relies on **pairwise comparison**, limiting scalability  
- No integration of **context-aware or reasoning-based analysis**

These limitations highlight the need for moving beyond structural detection toward **semantic-level understanding of programs**.

---

# 🚀 Research Direction

ACPDF serves as a foundational system for advancing toward **semantic and scalable code analysis**.

The next stage of this work focuses on bridging structural analysis with deeper program understanding.

### Proposed Directions

- **Semantic Program Representations**
  - Control Flow Graphs (CFG)
  - Program Dependence Graphs (PDG)

- **AI-Assisted Reasoning**
  - Integrating LLM-based models for reasoning about program behavior  
  - Detecting functional equivalence beyond syntax  

- **Scalable Code Retrieval Systems**
  - Indexing large repositories  
  - Efficient similarity search across codebases  

- **Agent-Based Architecture**
  - Modular analysis using specialized components  

### Key Research Questions

- How can semantic equivalence between programs be modeled effectively?  
- Can AI assist in reasoning about program behavior beyond structure?  
- How can such systems scale to real-world repositories?  

This work positions ACPDF at the intersection of **program analysis, information retrieval, and AI-assisted software engineering**.

---

# 📈 Performance Benchmark

| Method | Type | Score |
|-------|------|------|
| AST | Structural | 100% |
| LCS | Positional | 99.42% |
| TF-IDF | Statistical | 89.87% |
| **Hybrid** | Ensemble | **99.64%** |

---

# 🏗️ System Architecture

```

Input Code
↓
AST Parsing
↓
Feature Extraction
↓
Hybrid Scoring
↓
Similarity Report

```

---

# 🛠️ Tech Stack

| Layer | Technology |
|------|------------|
| Language | Java 21 |
| Framework | Spring Boot 3.3.4 |
| Parsing | JavaParser |
| Testing | JUnit 5 |
| DB | H2 / MySQL |

---

# 🚀 Getting Started

## Prerequisites
- Java 21+
- Maven

## Run Tests
```

mvn test

```

## Run Experiment
Execute:
```

ExperimentRunner.java

```

---

# 🧪 Why This Project Stands Out

- Combines **algorithms + system design + research**
- Not just implementation — **validated experimentally**
- Shows **deep understanding of program structure**
- Bridges **engineering + research thinking**

---

# 👨‍💻 Developer

**Ashish Patel**

- Backend Systems  
- AI Engineering  
- Algorithm Design  

---

<div align="center">

# 🌟 Final Statement

> ACPDF is not just a tool.  
> It is a **research-driven system exploring the future of code intelligence.**

<br>

# 🧬 ACPDF Detects What Others Miss.

</div>

