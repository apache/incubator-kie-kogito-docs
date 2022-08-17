<!-- Please don't forget your JIRA link -->
**JIRA:**

<!-- If you don't have a JIRA link, please provide a short description of what this PR does -->
**Description:**

<!-- Link to related PRs: -->

Please make sure that your PR meets the following requirements:

- [ ] You have read the [contributions doc](https://github.com/kiegroup/kogito-docs/blob/main/CONTRIBUTING.md)
- [ ] Pull Request title is properly formatted: `KOGITO-XYZ Subject`
- [ ] Pull Request title contains the target branch if not targeting main: `[0.9.x] KOGITO-XYZ Subject`
- [ ] The nav.adoc file has a link to this guide in the proper category
- [ ] The index.adoc file has a card to this guide in the proper category, with a meaningful description

<details>
<summary>
How to setup automatic cherry-pick to another branch ?
</summary>

The cherry-pick action allows to setup automatic cherry-pick from `main` to a specific branch.

To allow it, you will need to add the corresponding label with pattern: `backport-{RELEASE_BRANCH}`.  
For example, if a backport to branch `1.26.x` is needed, then the label should be `backport-1.26.x`.

Once the PR is merged, the action will retrieve the commit and cherry-pick it to the desired branch.

*NOTE: You can still add label after the merge and it should still be cherry-picked.*
</details>