# GitHub Pages Deployment Guide

This monorepo contains both backend (Spring Boot) and frontend (Astro) applications. This guide covers deploying the frontend to GitHub Pages using GitHub Actions.

## Automatic Deployment (GitHub Action)

### Prerequisites
- GitHub Actions enabled in your repository
- GitHub Pages enabled

### Setup Steps

1. **Enable GitHub Pages:**
   - Go to your repository on GitHub
   - Navigate to Settings → Pages
   - Under "Source", select "GitHub Actions"

2. **Push changes:**
   - Any push to the `main` branch that modifies files in the `frontend/` directory will trigger automatic deployment
   - The workflow will build and deploy your frontend automatically

3. **Monitor deployment:**
   - Go to the "Actions" tab in your repository to see deployment status
   - Your site will be available at: `https://<username>.github.io/<repo-name>/`

## Configuration Notes

- The frontend is configured to build from the `frontend/` directory
- The build output goes to `frontend/dist/`
- The GitHub Action only triggers on changes to the `frontend/` directory
- No manual steps required - just push code and it deploys automatically

## Troubleshooting

### GitHub Action Issues
- Check the Actions tab for error logs
- Ensure GitHub Pages is enabled in repository settings
- Verify the workflow file is in `.github/workflows/deploy.yml`

### Build Issues
- Ensure all dependencies are installed: `npm ci`
- Check for TypeScript/compilation errors
- Verify Astro configuration is correct

## Development vs Production

- **Development:** Use `npm run dev` for local development
- **Production:** Push to main branch - automatic deployment via GitHub Action
