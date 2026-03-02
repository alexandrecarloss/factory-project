# GitHub Repository Setup

## Prerequisites

- Git installed locally
- GitHub account
- GitHub CLI (optional but recommended) or git credentials configured

## Step 1: Create GitHub Repository

### Option A: Via GitHub Web Interface

1. Go to https://github.com/new
2. Repository name: `factory-project`
3. Description: "Factory Production Management System - Optimize production based on available raw materials"
4. Choose: Public (as per requirements)
5. DO NOT initialize with README (you already have one)
6. Click "Create repository"

### Option B: Via GitHub CLI

```bash
gh repo create factory-project \
  --description "Factory Production Management System" \
  --public \
  --remote=origin \
  --source=. \
  --remote-name=origin
```

## Step 2: Initialize Local Git Repository

```bash
cd factory-project

# Initialize git
git init

# Add all files
git add .

# Make first commit
git commit -m "feat: initial project structure

- Backend: Spring Boot API with PostgreSQL
- Frontend: Vue.js 3 with Vite
- Features: CRUD operations, production optimization algorithm
- Testing: JUnit 5 + Vitest
- Documentation: Complete setup guides"
```

## Step 3: Connect to GitHub

```bash
# Replace YOUR_USERNAME with your GitHub username
git remote add origin https://github.com/YOUR_USERNAME/factory-project.git

# Verify remote was added
git remote -v

# Rename branch (if needed)
git branch -M main

# Push to GitHub
git push -u origin main
```

## Step 4: Configure Repository Settings (GitHub Web)

### Branch Protection

1. Go to Settings → Branches
2. Add rule for `main`
3. Enable:
   - Require pull request reviews before merging
   - Require status checks to pass
   - Require branches to be up to date

### Actions (CI/CD)

1. Go to Actions
2. Verify workflow file exists at `.github/workflows/ci-cd.yml`
3. Workflows should trigger on push/PR

### Code Security

1. Go to Security → Code security and analysis
2. Enable Dependabot alerts
3. Enable secret scanning

## Step 5: Add Collaborators (Optional)

```bash
# Via GitHub web:
# Settings → Collaborators → Add people
# Or use GitHub CLI:
gh repo collaborators add USERNAME --permission maintain
```

## Step 6: Create Additional Branches (Optional)

```bash
# Development branch
git checkout -b develop
git push -u origin develop

# Create protection rule for develop as well:
# Go to Settings → Branches → Add rule
```

## Step 7: Upload to GitHub

### Using HTTPS

```bash
git push -u origin main
```

### Using SSH (Recommended)

```bash
# First setup SSH key (if not already done)
ssh-keygen -t ed25519 -C "your@email.com"

# Add to GitHub:
# Settings → SSH and GPG keys → New SSH key

# Then use SSH remote
git remote set-url origin git@github.com:YOUR_USERNAME/factory-project.git
git push -u origin main
```

## Step 8: Verify Repository

1. Go to https://github.com/YOUR_USERNAME/factory-project
2. Verify all files are present:
   - `/backend/api/` with all Java sources
   - `/frontend/` with all Vue components
   - `README.md`, `CONTRIBUTING.md`, `DEPLOYMENT.md`, etc.
3. Check GitHub Actions for successful workflow run

## Ongoing Git Workflow

### For Feature Development

```bash
# Create feature branch
git checkout -b feature/feature-name

# Make changes and commit
git add .
git commit -m "feat: description of feature"

# Push and create PR
git push origin feature/feature-name

# Go to GitHub and create Pull Request
# After review and approval, merge to main
```

### For Bug Fixes

```bash
# Create bug branch
git checkout -b fix/bug-description

# Make fixes
git add .
git commit -m "fix: description of fix"

# Push and create PR
git push origin fix/bug-description
```

### Regular Commit Message Format

```
<type>: <subject>

<body>

<footer>
```

**Types:**

- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation
- `style`: Code style (no logic changes)
- `refactor`: Code refactoring
- `perf`: Performance improvement
- `test`: Adding/updating tests
- `chore`: Maintenance

**Example:**

```
feat: add production calculation cache

- Cache optimized suggestions for 5 minutes
- Reduces database queries by 60%
- Add cache invalidation on inventory changes
- Add tests for cache behavior

Closes #123
```

## Repository Structure on GitHub

```
factory-project/
├── .github/
│   └── workflows/
│       └── ci-cd.yml
├── .gitignore
├── backend/
│   ├── api/
│   │   ├── src/
│   │   ├── pom.xml
│   │   └── Dockerfile
│   └── HELP.md
├── frontend/
│   ├── src/
│   ├── package.json
│   ├── vite.config.js
│   ├── Dockerfile
│   └── nginx.conf
├── docker-compose.yml
├── README.md
├── CONTRIBUTING.md
├── DEPLOYMENT.md
├── ARCHITECTURE.md
├── API_EXAMPLES.md
└── SAMPLE_DATA.md
```

## Repository README on GitHub

Your `README.md` will appear on the repository homepage and should include:

- ✅ Project description
- ✅ Quick start guide
- ✅ Prerequisites
- ✅ Installation steps
- ✅ Usage examples
- ✅ Testing information
- ✅ Deployment guide link
- ✅ Contributing guidelines link

## Adding Badges (Optional)

You can add badges to your README:

```markdown
# Repository Badges

[![Build Status](https://github.com/YOUR_USERNAME/factory-project/workflows/CI%2FCD/badge.svg)](https://github.com/YOUR_USERNAME/factory-project/actions)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/java-21+-orange.svg)](https://www.oracle.com/java/)
[![Vue](https://img.shields.io/badge/vue-3+-4FC08D.svg)](https://vuejs.org/)
[![Spring Boot](https://img.shields.io/badge/spring%20boot-4.0-6DB33F.svg)](https://spring.io/projects/spring-boot)
```

## Troubleshooting

### Authentication Issues

```bash
# If getting authentication errors:
git config --global user.name "Your Name"
git config --global user.email "your@email.com"

# For HTTPS, setup personal access token:
# GitHub Settings → Developer settings → Personal access tokens → Generate new token
# Use token as password when prompted
```

### Push Rejected

```bash
# If main branch doesn't exist remotely:
git push -u origin main

# If there are conflicts:
git pull origin main
# Resolve conflicts
git add .
git commit -m "merge: resolve conflicts"
git push origin main
```

### Large Files

If you accidentally commit large files:

```bash
# Use git filter-branch (careful!)
git filter-branch --tree-filter 'rm -f path/to/large/file' HEAD

# Or use git-lfs for future large files
```

## Collaboration Workflow

### For Team Members

1. Clone repository

```bash
git clone https://github.com/YOUR_USERNAME/factory-project.git
cd factory-project
```

2. Create feature branch

```bash
git checkout -b feature/your-feature
```

3. Make changes and push

```bash
git add .
git commit -m "feat: your feature"
git push origin feature/your-feature
```

4. Create Pull Request on GitHub
5. Wait for review and automatic checks to pass
6. Merge after approval

### Code Review Process

1. Request review from maintainers
2. Address feedback in new commits
3. Auto-CI/CD runs tests
4. Merge after approval

## GitHub Pages (Optional)

To host documentation:

1. Go to Settings → Pages
2. Source: Deploy from branch
3. Branch: `main` → `docs` folder
4. Your site will be at: https://YOUR_USERNAME.github.io/factory-project/

Create `/docs` folder with your documentation:

```bash
mkdir docs
cp README.md docs/index.md
cp ARCHITECTURE.md docs/
cp DEPLOYMENT.md docs/
```

## Release Management (Optional)

### Creating Releases

```bash
# Tag version
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0

# Or create release via GitHub web:
# Releases → Create new release
```

## Summary Checklist

- [ ] GitHub account created
- [ ] Repository created on GitHub
- [ ] Local git initialized with `git init`
- [ ] Remote added with `git remote add origin`
- [ ] All files committed with `git add .` and `git commit`
- [ ] Changes pushed with `git push -u origin main`
- [ ] Repository visible on GitHub
- [ ] README displays correctly
- [ ] GitHub Actions workflow configured
- [ ] Collaborators added (if needed)
- [ ] Branch protection rules configured
- [ ] Repository shared with the team

## Repository Link

After pushing, your repository will be available at:

```
https://github.com/YOUR_USERNAME/factory-project
```

Share this link with your team! 🎉
