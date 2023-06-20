# Contributing

We accept all kinds of contributions:

1. Reviewing a PR
2. Opening [an issue](issues) by describing what problem you see that we need to fix
3. Opening [a PR](pulls) if you see a typo, broken link, or any other minor changes.

> To include a new guide or documentation content, please **open an issue first** so we can discuss in more detail what needs to be done. We use [JIRA](https://issues.redhat.com/) to track our tasks, so the issue is likely to turn into a JIRA issue.

## Including a new guide

1. Open a JIRA issue and add a sub-task for Doc review. 
2. Write the guide
3. Add a link to the guide in [serverlessworkflow/modules/ROOT/nav.adoc](serverlessworkflow/modules/ROOT/nav.adoc)
4. Add a card for the guide in [serverlessworkflow/modules/ROOT/pages/index.adoc](serverlessworkflow/modules/ROOT/pages/index.adoc)

## Opening a JIRA Issue

1. Make sure to add a description of the guide you plan to add followed by the `[Kogito Guides]` prefix
2. Clearly describe in which parent category you will publish the guide
3. Create a sub-task with the title `[Docs Review][Kogito Guides] - JIRA Title` so that the Content Team can review your guide.
4. After all SMEs have reviewed and approved your guide, change the status of the sub-task to "Pull Request Sent" by adding the PR link to the sub-task JIRA. This way, the documentation team will be notified and will review your PR.

### Following up a code change

If your PR is to update a guide with a change you made in Kogito project code base, you don't need to create a new JIRA just to update the documentation.

Use the same JIRA issue and make sure that your branch is called `kogito-NNNNN` where `NNNNN` is the JIRA number. This way, our automation will work and link all the PRs together among every repository impacted by your change.

For the documentation review, do the same step as described in the item number 4 in the previous section: open a sub-task in the JIRA issue.

## Basic Conventions

As a general rule of thumb, look at the [published documentation](https://kiegroup.github.io/kogito-docs/) to have an idea of the writing style, format, and organization.

### UI Elements

When you write about an item displayed in a graphical user interface, match the capitalization and spelling of the item, and bold the term in your writing. For example:

:x: Click **Save As...** and then type a file name.  
:x: Click save as and then type a file name.  
:white_check_mark: Click **Save As** and then type a file name.

### Spelling

In general, use US English spelling in all publications.

:x: Labelled  
:white_check_mark: Labeled

:x: Fulfil  
:white_check_mark: Fulfill

### Headings

For a task procedure heading, use a gerund or imperative verb form. Use a gerund for a high-level task such as installing, administering, or troubleshooting.

:x: Create a data load activity  
:white_check_mark: Creating a data load activity

Ensure that the typographic style is consistent for each heading level in your content.

:x: Mapping assets by using the cf map-route command  
:white_check_mark: Mapping assets by using the `cf map-route` command

Always use the following convention when creating a new article for the main heading:

```asciidoc
= Kogito Guide Title (first-level heading)
== Example section (second-level heading)
...
```

### Writing style

Use present tense.

:x: When you open the latch, the panel will slide forward.  
:white_check_mark: When you open the latch, the panel slides forward.

Use active voice.

:x: _Passive:_ The Limits window is used to specify the minimum and maximum values.  
:white_check_mark: _Active:_ In the Limits window, specify the minimum and maximum values.

Use second person (you). Avoid first person (I, we, us). Be gender neutral. Use the appropriate tone. Write for a global audience.

:x: We can add a model to the project that we created in the previous step.  
:white_check_mark: You can add a model to the project that you created in the previous step.

:x: It is important that the file be saved...  
:white_check_mark: Important: Save the file...  

### Capitalization

In general, use a lowercase style in text and use sentence-style capitalization for headings, titles, labels, banners, and similar elements.

:white_check_mark: Business models  
:white_check_mark: Creating Boolean expressions  
:white_check_mark: Planning network architectures  
:white_check_mark: Properties and settings for printing  
:white_check_mark: Requirements for Linux and UNIX operating systems

## Quick Reference

### Page cross-reference

- To refer a page in same module
  - `xref:file_name.adoc[optional text]`
  - Example: `xref:index.adoc[Home]`
- To refer a page in different module
  - `xref:module_name:file_name.adoc[optional text]`
  - Example: `xref:getting-started:create-file.adoc[Create file]`
- To refer a page on other component
  - `xref:compnent_name:module_name:file_name.adoc[optional text]`
  - Example: `xref:contribution-guide:getting-started:create-file.adoc[Create file]`

More details regarding xref at the [Antora documentation xref section](https://docs.antora.org/antora/latest/page/xref).

### Embedding a page in current page

- Embed page in same module
  - `include::./gear.adoc[optionl text]`
- Embed a page from another module or component version
  - `include::module:file-coordinate-of-target-page.adoc[]`
  - `include::version@component:module:file-coordinate-of-target-page.adoc[]`
  - `include::component:module:file-coordinate-of-target-page.adoc[]`

More details at [Antora documentation](https://docs.antora.org/antora/latest/page/include-a-page).

### Assigning attributes on a site

You can create attributes in `{project_root}/{component_name}/antora.yml` file. These attributes can be use anywhere in that module.

```yaml
asciidoc:
  attributes:
    :example_url: https://www.myexample.com
```

More details at [Antora documentation](https://docs.antora.org/antora/latest/playbook/asciidoc-attributes)

### Using Dry URLs for Links

You should assign the URL to a short, easy to remember attribute. For example:

```asciidoc
:issues_url: https://github.com/asciidoctor/asciidoctor/issues`

// later in the document

Submit bug fixes to the link:{issues_url}[issue tracker]
```

> Every attribute which consists of a URL must have the suffix `_url`. Use underscore (`_`) to separate words.

More details at the [AsciiDoc Documentation](https://asciidoctor.org/docs/asciidoc-recommended-practices/#dry-urls)

### Creating new category for docs

In the same component to add a new category, create a new folder with a category name under the `modules/ROOT/pages/` folder of the component.

For example, to add a page `hello.adoc` you can create a page at `modules/ROOT/pages/hello/hello.adoc`.

### Storing Assets

Assets relating to a page should be stored at `modules/ROOT/assets` under the given category.

For example, to add an image for `hello.adoc` page, put the image in
`modules/ROOT/assets/images/hello/image_name.png`

### Creating switch tabs

You can create tabs using the format below:

```asciidoc
[tabs]
====
Tab A::
+
[source,shell]
--
Contents of tab A.
--
Tab B::
+
[source,java]
--
Contents of tab B.
--
====
```

### Adding an admonitions

Use the following format:

```asciidoc
[NOTE]
====
Content
====
```

Similarly you can have other admonitions:

- `TIP`
- `IMPORTANT`
- `CAUTION`
- `WARNING`

More details at [Antora documentation](https://docs.antora.org/antora/latest/asciidoc/admonitions/).

## Generating Release Notes for Serverless Workflow

1. You can retrieve all changes in the release page of each repository of the project:

   - https://github.com/kiegroup/kogito-runtimes/releases/tag/{version}
   - https://github.com/kiegroup/kogito-apps/releases/tag/{version}
   - https://github.com/kiegroup/kogito-examples/releases/tag/{version}
   - https://github.com/kiegroup/kogito-runtimes/releases/tag/{version_without_Final}
   - https://github.com/kiegroup/kogito-runtimes/releases/tag/v{version_without_Final}

Replace `{version}` with the given core version, for example `1.40.0.Final`.  
Replace `{version_without_Final}` with the given cloud version, for example `1.40.0`.

2. Update the page [release_notes.adoc](serverlessworkflow/modules/ROOT/pages/release_notes.adoc)

   You should get JIRA issues with `KOGITO` and `DROOLS` projects as well as issues coming from the `kie-issues` repository.

4. Align with the team what should be under "Notable changes"
5. Add the rest to "Other Changes and Bug Fixing".
6. Open a PR in the target **branch** version, **not** main
7. Add one member from each squad to review

## Useful Resources

- [AsciiDoc Syntax Quick Reference](https://docs.asciidoctor.org/asciidoc/latest/syntax-quick-reference/)
- [Antora Documentation](https://docs.antora.org/antora/latest/)
