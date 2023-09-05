<!-- Please don't forget your JIRA link -->
**JIRA:**

<!-- If you don't have a JIRA link, please provide a short description of what this PR does -->
**Description:**

<!-- Link to related PRs: -->

Please make sure that your PR meets the following requirements:

- [ ] You have read the [contributions doc](https://github.com/apache/incubator-kie-kogito-docs/blob/main/CONTRIBUTING.md)
- [ ] Pull Request title is properly formatted: `KOGITO-XYZ Subject`
- [ ] Pull Request title contains the target branch if not targeting main: `[0.9.x] KOGITO-XYZ Subject`
- [ ] The nav.adoc file has a link to this guide in the proper category
- [ ] The index.adoc file has a card to this guide in the proper category, with a meaningful description

<details>
<summary>
How to backport a pull request to a different branch?
</summary>

In order to automatically create a **backporting pull request** please add one or more labels having the following format `backport-<branch-name>`, where `<branch-name>` is the name of the branch where the pull request must be backported to (e.g., `backport-7.67.x` to backport the original PR to the `7.67.x` branch).

> **NOTE**: **backporting** is an action aiming to move a change (usually a commit) from a branch (usually the main one) to another one, which is generally referring to a still maintained release branch. Keeping it simple: it is about to move a specific change or a set of them from one branch to another.

Once the original pull request is successfully merged, the automated action will create one backporting pull request per each label (with the previous format) that has been added.

If something goes wrong, the author will be notified and at this point a manual backporting is needed.

> **NOTE**: this automated backporting is triggered whenever a pull request on `main` branch is labeled or closed, but both conditions must be satisfied to get the new PR created.
</details>