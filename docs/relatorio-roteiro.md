# Roteiro do relatório

Roteiro das seções do relatório técnico (PDF entregue no AVA). O relatório em si, com nomes completos e matrículas, **não fica neste repositório**: ele é escrito fora daqui e entregue apenas no AVA.

Formato: padrão ABNT (modelo da Unifor), até 10 páginas, uma coluna. Figuras e quadros numerados, com legenda acima e fonte abaixo.

## Estrutura

| Parte | Conteúdo | Material de apoio no repositório |
|---|---|---|
| Primeira página | Título, integrantes, objetivo, link do repositório e disciplina | — |
| 1 Apresentação | Problema, objetivo, público, escopo (e o que ficou fora dele), cenário da equipe e organização do trabalho | `README.md`, `docs/testes.md` (cenário próprio) |
| 2 Modelagem e organização | Diagrama de classes (Figura 1), responsabilidades das classes (Quadro 1), conceitos de POO aplicados (Quadro 2), relações e multiplicidades (Quadro 3), organização MVC (Figura 2) e a limitação conhecida | `docs/uml.md`, `docs/img/uml-modelo.png`, `docs/img/uml-mvc.png` |
| 3 Implementação | Cálculos em centavos e distribuição dos centavos, validações e mensagens, coleções (Quadro 4), persistência por serialização e interface gráfica (layouts e eventos) | `docs/uml.md` (decisões e justificativa das coleções), código em `src/` |
| 4 Testes e resultados | Os sete testes do enunciado e o cenário próprio: entrada, esperado, obtido e evidência | `docs/testes.md`, `docs/capturas/` |
| 5 Avaliação e melhorias | Decisões de POO, dificuldades, correções, limitações e melhorias propostas; contribuições dos integrantes; declarações (ferramenta de conferência e uso de IA) | `docs/uml.md` (limitação do MVC), `docs/testes.md` (observações) |
| Referências | Enunciado, material da disciplina, documentação do Java, normas ABNT e demais fontes | — |

## Justificativas usadas no relatório

As justificativas das **associações** (composição, associação, herança, realização e dependência, com as multiplicidades) e das **coleções** (`List`/`ArrayList`, e por que não `Set` ou `Map`) estão em [`docs/uml.md`](uml.md), nas seções "Justificativa das relações do modelo" e "Justificativa das coleções". O relatório resume essas justificativas nos Quadros 3 e 4.
