# Copilot Instructions for MCP Server

This repository contains a Model Context Protocol (MCP) server for the `todo-service` application.  The server can be implemented in any supported language, but by default the SDK is TypeScript.  See the official documentation for details:

- https://github.com/modelcontextprotocol
- https://modelcontextprotocol.io/llms-full.txt

Follow these steps when working on or extending the MCP server:

1. Use the MCP SDK appropriate to the chosen language (TypeScript/JavaScript/Python/C#/Java/Kotlin).
2. Implement the server's dependencies using the appropriate package manager (e.g. `package.json` for Node/TypeScript or `requirements.txt` for Python).
3. Place source code under a `server/` or `src/` folder and ensure there is an entry point (e.g. `index.ts` or `server.py`).
4. Update `.vscode/mcp.json` with the command to start the server so VS Code can launch and debug it. Adjust the `command` to `python` and arguments such as `['server/server.py']` when using Python.
5. Use the VS Code debugger and the MCP protocol to step through interactions.

The MCP server is intended to provide context information to Copilot requests related to the todo-service, such as file contents, configuration, or domain-specific knowledge.
