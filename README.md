# Java4Shell Lab — CVE-2026-114938

![Abstract Java decoder memory-safety lab illustration](lab-hero.png)

A small, isolated educational lab for examining the OpenJDK Base64 decoder issue tracked as CVE-2026-114938. The lab is designed to show whether a selected JVM reproduces the vendor-described decoder failure and how the supported mitigation changes the result. It is not a remote exploit, target scanner, or proof of code execution.

> **Research credit:** CVE-2026-114938 was reported by Quang Luong of Calif.io, with Albert Yang also credited by Oracle. This repository is an independent educational reproduction. It links the original research and vendor advisories below; it does not claim discovery of the vulnerability.

## Safety and scope

Run this only on a disposable local machine using the supplied container. The vulnerable JVM is intentionally pinned for demonstration. The container has no network access at runtime, drops privileges, uses a read-only root filesystem, and has resource limits. The trigger is a single bounded decoder input; it does not attempt heap grooming, data disclosure, or remote code execution.

Do not run the vulnerable runtime in production or against third-party systems. A non-reproducing result is **inconclusive**, not a safety verdict: host CPU features, JVM build, runtime flags, and vendor backports affect the outcome.

## Requirements

- Docker Engine with Compose v2
- x86-64 host with AVX-512 VBMI support for reproduction
- Network access only while Docker downloads the pinned image; the runtime container is offline

## Run

```sh
docker compose -f lab/compose.yaml build
docker compose -f lab/compose.yaml run --rm --no-deps java4shell-lab
```

The program prints the JVM version and architecture. It checks for changes past the decoder's expected output region and prints the result. It never contacts a target or opens a listener.

To run the vendor mitigation comparison in the same container:

```sh
docker compose -f lab/compose.yaml run --rm --no-deps java4shell-mitigated
```

The mitigation is a diagnostic comparison, not a replacement for installing a fixed vendor build. Prefer upgrading to a supported patched Java release and rebuilding/restarting workloads.

## What this demonstrates

The reported issue is in an optimized OpenJDK Base64 decode path on qualifying x86-64 systems. A carefully selected malformed input may expose a memory-safety failure in affected JVM builds. Reproduction depends on the exact vendor build, CPU feature set, and effective JVM options. This lab deliberately stops at a local, bounded memory-integrity observation; it does not establish exploitability in any application.

## References

- [CVE-2026-114938 record](https://www.cve.org/CVERecord?id=CVE-2026-114938)
- [OpenJDK vulnerability advisory](https://openjdk.org/groups/vulnerability/advisories/2026-11-17)
- [Oracle November 2026 Critical Security Patch Update](https://www.oracle.com/security-alerts/cspunov2026.html)
- [AWS security bulletin and Corretto fixed versions](https://aws.amazon.com/security/security-bulletins/2026-189-aws/)
- [CERT-EU advisory](https://www.cert.europa.eu/publications/security-advisories/2026-021/)
- [Original research project](https://github.com/ikow/java4shell-poc) — background and further research by its authors; not a source repository for this implementation.

## License

MIT. See [LICENSE](LICENSE).

