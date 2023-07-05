# Compiler Project

This project is a compiler implementation that covers various stages of the compilation process. It provides functionality for converting regular expressions to nondeterministic finite automata (NFA), transforming NFAs to deterministic finite automata (DFA), handling fallback DFAs, eliminating epsilon and unit productions in context-free grammars (CFG), and implementing a L1 parser for CFGs.

## Project Tasks

The following tasks have been implemented in this project:

1. **RegextoNFA**: This task involves converting regular expressions to nondeterministic finite automata (NFA). The implementation should be able to handle different types of regular expressions and generate the corresponding NFAs.

2. **NFAtoDFA**: In this task, the NFA generated from the regular expressions is transformed into a deterministic finite automaton (DFA). This conversion simplifies the automaton and makes it easier to analyze and process.

3. **FallbackDFA**: The project includes functionality for handling fallback DFAs. A fallback DFA is a modified version of a DFA that can fall back to a previous state if the input does not match the current state. This feature is often used in error recovery mechanisms in compilers.

4. **CFGEpsilonunitElimination**: This task involves eliminating epsilon and unit productions in context-free grammars (CFG). Epsilon productions are those that can produce an empty string, while unit productions consist of a single nonterminal on the right-hand side. Removing these types of productions simplifies the grammar and improves parsing efficiency.

5. **CFGleftRecElimination**: The implementation includes a procedure for eliminating left recursion in context-free grammars (CFG). Left recursion occurs when a nonterminal can derive itself directly or indirectly from the left side of a production rule. Removing left recursion is necessary to avoid infinite loops during parsing.

6. **CFGfirstFollow**: This task involves computing the first and follow sets for the nonterminals in the CFG. The first set of a nonterminal consists of the terminals that can appear as the first symbol in any derivation of that nonterminal. The follow set of a nonterminal consists of the terminals that can appear immediately after the nonterminal in any derivation.

7. **CFGL1 parser**: The project includes the implementation of an L1 parser for CFGs. An L1 parser uses a lookahead of one token to decide which production rule to apply during parsing. This parser can efficiently handle a wide range of context-free grammars.

## Getting Started

To use this project, follow the steps below:

1. Clone the repository: `git clone https://github.com/your/repository.git`
2. Run the tests provided in the tests package

## Contribution Guidelines

If you want to contribute to this project, please follow these guidelines:

1. Fork the repository and create a new branch for your contribution.
2. Ensure that your code follows the project's coding style and conventions.
3. Write clear and concise commit messages.
4. Test your changes thoroughly before submitting a pull request.
5. Provide a detailed description of your changes in the pull request.
