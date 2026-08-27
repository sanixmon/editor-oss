# Editor OSS

An original, fast Android image editor inspired by the approachable spirit of classic mobile editors. This is a spiritual successor, not a copy: no proprietary names, logos, code, or assets are used.

## Product direction

- Open a photo and start editing immediately.
- Tap-first tools for effects, stickers, text, bubbles, shapes, and drawing.
- Object-based composition: every image, sticker, text, bubble, shape, and effect can share transform, opacity, selection, duplicate, and delete behavior.
- Non-destructive commands with unlimited undo/redo.
- GPU preview with CPU processing/export fallback.

## Modules

- `core:model`, `core:geometry`, `core:editor`, `core:history`, `core:common`
- `rendering:renderer`, `rendering:opengl`
- `image:decoder`, `image:processor`, `image:exporter`
- `data:project`, `data:media`
- `feature:editor`
- `app`

The domain modules do not depend on Compose, Activities, OpenGL, or persistence implementations.

## CI-first workflow

GitHub Actions runs the Android build, lint, and tests, then uploads the debug APK. The Gradle Wrapper will be committed before the first CI build; local development can therefore remain lightweight while GitHub-hosted runners provide the Android SDK and build resources.

## Current scope

Phase 1 contains the multi-module setup, object/effect model, geometry primitives, editor commands, undo/redo foundation, renderer/effect abstractions, and a basic Compose shell. GPU compositing, gestures, persistence, and export are next.
