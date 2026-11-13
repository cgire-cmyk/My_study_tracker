<div align="center">

# 🎓 MY STUDY TRACKER

**Sistema en Java que ayuda al estudiante a saber cuánto necesita para aprobar, si sus metas son posibles y cómo organizar su estudio.**

![Java](https://img.shields.io/badge/Java-8%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![POO](https://img.shields.io/badge/Paradigma-POO-1F6FEB?style=for-the-badge)
![Consola](https://img.shields.io/badge/Interfaz-Consola-2EA043?style=for-the-badge)
![Estado](https://img.shields.io/badge/Estado-2.º%20avance-8250DF?style=for-the-badge)



</div>

---

##  Datos académicos

| | |
|---|---|
| **Universidad** | Universidad Nacional de San Agustín de Arequipa |
| **Escuela profesional** | Ingeniería de Sistemas (EPIS) |
| **Curso** | Desarrollo de Software Orientado a Objetos |
| **Docente** | ING. MARCO WILFREDO AEDO LOPEZ |
| **Semestre** | 2026-II |
| **Entrega** | Segundo avance: código fuente ejecutable en consola |

---

##  Presentación

Muchos estudiantes llegan al examen final sin saber **qué nota necesitan** para aprobar un curso, o descubren
demasiado tarde que una meta (tercio superior, quinto superior, beca) ya no era posible.

**MY STUDY TRACKER** resuelve ese problema: el estudiante registra sus cursos,
evaluaciones y notas, y el sistema **calcula, predice y recomienda** en tiempo real.

###  Objetivos

- **General:** desarrollar un sistema orientado a objetos que apoye al estudiante en el seguimiento y la predicción de su rendimiento académico.
- **Específicos:**
  - Calcular la nota mínima necesaria en las evaluaciones pendientes de cada curso.
  - Determinar si las metas académicas son alcanzables o imposibles.
  - Identificar los cursos en riesgo y emitir alertas por nivel.
  - Generar recomendaciones y un horario de estudio según el riesgo de cada curso.
---

## Equipo de desarrollo · Chachani Dev

<table>
  <tr>
    <td align="center" width="33%">
      <img src="https://img.shields.io/badge/Módulo%201-Núcleo%20académico-1F6FEB?style=flat-square" alt="Módulo 1"/><br/><br/>
      <b>JOSÉ JUAN CHOQUEMAMANI CALISAYA<br/><sub>Líder del equipo</sub></b><br/>
    </td>
    <td align="center" width="33%">
      <img src="https://img.shields.io/badge/Módulo%202-Estudiante%20y%20metas-2EA043?style=flat-square" alt="Módulo 2"/><br/><br/>
      <b>ARNOLD MARCO ANTONIO CHOQUE MENDOZA</b><br/>
    </td>
    <td align="center" width="33%">
      <img src="https://img.shields.io/badge/Módulo%203-Análisis%20y%20consola-E8590C?style=flat-square" alt="Módulo 3"/><br/><br/>
      <b>CRISTHIAN HERNÁN GIRE MAITA</b><br/>
    </td>
  </tr>
  <tr>
    <td valign="top"><sub><code>Curso</code>, <code>Evaluacion</code>, <code>PracticaCalificada</code>, <code>Trabajo</code>, <code>ExamenParcial</code>, <code>Calificacion</code></sub></td>
    <td valign="top"><sub><code>Estudiante</code>, <code>Meta</code>, <code>MetaAprobacion</code>, <code>MetaPromedio</code>, <code>HorarioEstudio</code>, <code>BloqueEstudio</code></sub></td>
    <td valign="top"><sub><code>Alertable</code>, <code>Recomendable</code>, <code>Alerta</code>, <code>GestorAlertas</code>, <code>GestorRecomendaciones</code>, <code>InterfazConsola</code>, <code>Main</code></sub></td>
  </tr>
</table>

---
##  Arquitectura

El diagrama de clases UML completo está en [`docs/diagrama_clases_uml.pdf`](docs/diagrama_clases_uml.pdf).

### Estructura del repositorio

```
plataforma-academica/
├── 📄 README.md
├── 📂 docs/
│   └── diagrama_clases_uml.pdf
└── 📂 src/
    ├── 📂 nucleo/             Módulo 1 · Núcleo académico
    ├── 📂 planificacion/      Módulo 2 · Estudiante, metas y horario
    ├── 📂 analisis/           Módulo 3 · Alertas y recomendaciones
    └── 📂 consola/            Módulo 3 · Interfaz de consola y Main
```

## Como ejecutar

### 1️ Descargar el proyecto

**Opción A – Descargar ZIP:** en la página del repositorio, pulsa **Code → Download ZIP** y descomprime el archivo.

**Opción B – Clonar con Git:**

```bash
git clone https://github.com/cgire-cmyk/My_study_tracker.git
```

### 2️ Ejecutar

> La clase principal es **`consola.Main`**, en `src/consola/Main.java`.
> Las clases están organizadas en paquetes (`nucleo`, `planificacion`, `analisis`, `consola`), así que la carpeta
> **`src`** debe ser la carpeta de fuentes del proyecto.
> vs code

## Avances del proyecto

- [x] **Avance 1:** diagrama de clases UML
- [x] **Avance 2:** código fuente ejecutable en consola, organizado en GitHub
- [ ] **Avance 3:** `[por definir]`
- [ ] **Entrega final:** `[por definir]`


