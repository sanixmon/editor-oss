# Editor OSS

An original, fast Android image editor inspired by the approachable spirit of classic mobile editors. This is a spiritual successor, not a copy: no proprietary names, logos, code, or assets are used.

## Milestone 0 foundation

The first stable pipeline is intentionally small:

`Import Photo → Create Project → ImageObject → Texture → Render → Pan/Zoom → Transform → Undo/Redo → Export`

The current code provides the contracts and state transitions for this pipeline. Android bitmap decoding, GLES texture upload, framebuffer compositing, gestures, and file export are isolated behind interfaces so they can be implemented incrementally without contaminating the editor core.

## Modules

- `core/model`, `core/geometry`, `core/editor`, `core/history`, `core/common`
- `engine/renderer`, `engine/opengl`, `engine/texture`, `engine/compositor`
- `image/decoder`, `image/exporter`
- `feature/editor`, `app`

The domain modules do not depend on Compose, Activities, OpenGL, or persistence implementations.

## Product direction

- Open a photo and start editing immediately.
- Tap-first tools for effects, stickers, text, bubbles, shapes, and drawing.
- Object-based composition: every object shares transform, opacity, selection, duplicate, and delete behavior.
- Non-destructive commands with unlimited undo/redo.
- GPU preview with CPU processing/export fallback.

## CI-first workflow

GitHub Actions installs Gradle 8.11.1, runs the Android build, lint, and tests, then uploads the debug APK. Local development can remain lightweight while GitHub-hosted runners provide the Android SDK and build resources.
